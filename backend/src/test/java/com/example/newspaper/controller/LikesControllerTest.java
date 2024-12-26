package com.example.newspaper.controller;

import com.example.newspaper.model.Article;
import com.example.newspaper.model.User;
import com.example.newspaper.security.JwtTokenUtil;
import com.example.newspaper.service.ArticleService;
import com.example.newspaper.service.LikeService;
import com.example.newspaper.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class LikesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LikeService likeService;

    @Mock
    private ArticleService articleService;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private LikesController likesController;

    @BeforeEach
    void setUp() {
        // Инициализация MockMvc для тестирования контроллера
        mockMvc = MockMvcBuilders.standaloneSetup(likesController).build();
    }

    @Test
    void shouldLikeArticle() throws Exception {
        Long articleId = 1L;
        String token = "Bearer valid-jwt-token";

        // Создаем объект User через сеттеры
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        Article article = new Article();
        article.setId(articleId);

        // Мокирование поведения сервисов
        when(jwtTokenUtil.getUsernameFromToken("valid-jwt-token")).thenReturn("john.doe@example.com");
        when(userService.findByEmail("john.doe@example.com")).thenReturn(user);
        when(articleService.getArticleById(articleId)).thenReturn(article);

        // Выполнение запроса и проверка результата
        mockMvc.perform(MockMvcRequestBuilders.post("/likes/article/{articleId}", articleId)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Liked successfully"));

        // Проверка, что метод addLike был вызван
        verify(likeService, times(1)).addLike(user, article);
    }

    @Test
    void shouldUnlikeArticle() throws Exception {
        Long articleId = 1L;
        String token = "Bearer valid-jwt-token";

        // Создаем объект User через сеттеры
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        Article article = new Article();
        article.setId(articleId);

        // Мокирование поведения сервисов
        when(jwtTokenUtil.getUsernameFromToken("valid-jwt-token")).thenReturn("john.doe@example.com");
        when(userService.findByEmail("john.doe@example.com")).thenReturn(user);
        when(articleService.getArticleById(articleId)).thenReturn(article);

        // Выполнение запроса и проверка результата
        mockMvc.perform(MockMvcRequestBuilders.delete("/likes/article/{articleId}", articleId)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Unliked successfully"));

        // Проверка, что метод removeLike был вызван
        verify(likeService, times(1)).removeLike(user, article);
    }

    @Test
    void shouldGetLikesCount() throws Exception {
        Long articleId = 1L;
        Article article = new Article();
        article.setId(articleId);
        int likeCount = 5;

        // Мокирование поведения сервисов
        when(articleService.getArticleById(articleId)).thenReturn(article);
        when(likeService.getLikesCount(article)).thenReturn(likeCount);

        // Выполнение запроса и проверка результата
        mockMvc.perform(MockMvcRequestBuilders.get("/likes/article/{articleId}/count", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(likeCount));
    }

    @Test
    void shouldCheckIfArticleIsLikedByUser() throws Exception {
        Long articleId = 1L;
        String token = "Bearer valid-jwt-token";

        // Создаем объект User через сеттеры
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        Article article = new Article();
        article.setId(articleId);
        boolean isLiked = true;

        // Мокирование поведения сервисов
        when(jwtTokenUtil.getUsernameFromToken("valid-jwt-token")).thenReturn("john.doe@example.com");
        when(userService.findByEmail("john.doe@example.com")).thenReturn(user);
        when(articleService.getArticleById(articleId)).thenReturn(article);
        when(likeService.isArticleLikedByUser(user, article)).thenReturn(isLiked);

        // Выполнение запроса и проверка результата
        mockMvc.perform(MockMvcRequestBuilders.get("/likes/article/{articleId}/user", articleId)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(isLiked));
    }
}
