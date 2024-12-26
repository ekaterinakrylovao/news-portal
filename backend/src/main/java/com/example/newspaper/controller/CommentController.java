package com.example.newspaper.controller;

import com.example.newspaper.dtos.CommentDto;
import com.example.newspaper.model.Article;
import com.example.newspaper.model.Comment;
import com.example.newspaper.security.JwtTokenUtil;
import com.example.newspaper.service.ArticleService;
import com.example.newspaper.service.CommentService;
import com.example.newspaper.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final ArticleService articleService;
    private final JwtTokenUtil jwtTokenUtil;

    public CommentController(CommentService commentService, UserService userService, ArticleService articleService, JwtTokenUtil jwtTokenUtil) {
        this.commentService = commentService;
        this.userService = userService;
        this.articleService = articleService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<CommentDto>> getCommentsByArticle(@PathVariable Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticle(articleId);
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getTimestamp().toString(),
                        comment.getUser().getFirstName()))
                .toList();
        return ResponseEntity.ok(commentDtos);
    }

    // Добавление комментария (только авторизованные пользователи)
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody Comment comment, @RequestParam Long articleId, @RequestHeader("Authorization") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7)); // Получение email из токена
        comment.setUser(userService.findByEmail(username)); // Установка пользователя

        Article article = articleService.getArticleById(articleId); // Получение статьи по ID
        if (article == null) {
            return ResponseEntity.badRequest().body("Article not found");
        }

        comment.setArticle(article); // Установка статьи
        comment.setTimestamp(LocalDateTime.now()); // Установка времени комментария

        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(savedComment);
    }
}
