package com.example.newspaper.controller;

import com.example.newspaper.security.JwtTokenUtil;
import com.example.newspaper.service.ArticleService;
import com.example.newspaper.service.LikeService;
import com.example.newspaper.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikesController {

    private final LikeService likeService;
    private final ArticleService articleService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public LikesController(LikeService likeService, ArticleService articleService, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.likeService = likeService;
        this.articleService = articleService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Добавить лайк к статье
    @PostMapping("/article/{articleId}")
    public ResponseEntity<?> likeArticle(@PathVariable Long articleId, @RequestHeader("Authorization") String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        var user = userService.findByEmail(email);
        var article = articleService.getArticleById(articleId);

        likeService.addLike(user, article);
        return ResponseEntity.ok("Liked successfully");
    }

    // Удалить лайк с статьи
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<?> unlikeArticle(@PathVariable Long articleId, @RequestHeader("Authorization") String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        var user = userService.findByEmail(email);
        var article = articleService.getArticleById(articleId);

        likeService.removeLike(user, article);
        return ResponseEntity.ok("Unliked successfully");
    }

    // Получить количество лайков для статьи
    @GetMapping("/article/{articleId}/count")
    public ResponseEntity<Integer> getLikesCount(@PathVariable Long articleId) {
        var article = articleService.getArticleById(articleId);
        int count = likeService.getLikesCount(article);
        return ResponseEntity.ok(count);
    }

    // Проверить, поставил ли текущий пользователь лайк на статье
    @GetMapping("/article/{articleId}/user")
    public ResponseEntity<Boolean> isArticleLikedByUser(@PathVariable Long articleId, @RequestHeader("Authorization") String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        var user = userService.findByEmail(email);
        var article = articleService.getArticleById(articleId);

        boolean isLiked = likeService.isArticleLikedByUser(user, article);
        return ResponseEntity.ok(isLiked);
    }

}
