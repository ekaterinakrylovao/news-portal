package com.example.newspaper.controller;

import com.example.newspaper.model.User;
import com.example.newspaper.model.Article;
import com.example.newspaper.model.Comment;
import com.example.newspaper.security.JwtTokenUtil;
import com.example.newspaper.service.ArticleService;
import com.example.newspaper.service.CommentService;
import com.example.newspaper.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private UserService userService;

    @Mock
    private ArticleService articleService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void shouldGetCommentsByArticle() throws Exception {
        // Подготовка данных для теста
        Long articleId = 1L;
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("This is a comment");
        comment.setTimestamp(LocalDateTime.now());

        // Создание пользователя
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        comment.setUser(user);  // Присваиваем комментарий пользователю

        List<Comment> comments = List.of(comment);

        // Мокирование вызова сервиса
        when(commentService.getCommentsByArticle(articleId)).thenReturn(comments);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/comments/article/{articleId}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].content").value("This is a comment"))
                .andExpect(jsonPath("$[0].username").value("John"));  // Проверка, что имя пользователя в комментарии верное
    }

    @Test
    void shouldAddCommentSuccessfully() throws Exception {
        // Подготовка данных для теста
        String token = "Bearer valid_token";
        Long articleId = 1L;
        Comment comment = new Comment();
        comment.setContent("New comment content");

        Article article = new Article();
        article.setId(articleId);
        article.setTitle("Article Title");

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent("New comment content");
        savedComment.setTimestamp(LocalDateTime.now());
        savedComment.setArticle(article);

        // Мокирование вызовов сервисов
        when(jwtTokenUtil.getUsernameFromToken(anyString())).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(null); // Предположим, что этот пользователь существует
        when(articleService.getArticleById(articleId)).thenReturn(article);
        when(commentService.saveComment(any(Comment.class))).thenReturn(savedComment);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .param("articleId", String.valueOf(articleId))
                        .content("{\"content\":\"New comment content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("New comment content"));
    }

    @Test
    void shouldReturnBadRequestIfArticleNotFound() throws Exception {
        // Подготовка данных для теста
        String token = "Bearer valid_token";
        Long articleId = 999L; // Несуществующий ID статьи
        Comment comment = new Comment();
        comment.setContent("Comment for non-existing article");

        // Мокирование вызовов сервисов
        when(jwtTokenUtil.getUsernameFromToken(anyString())).thenReturn("user@example.com");
        when(userService.findByEmail("user@example.com")).thenReturn(null); // Предположим, что этот пользователь существует
        when(articleService.getArticleById(articleId)).thenReturn(null); // Статья не найдена

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .param("articleId", String.valueOf(articleId))
                        .content("{\"content\":\"Comment for non-existing article\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Article not found"));
    }
}
