package com.example.newspaper.controller;

import com.example.newspaper.dtos.ArticleDto;
import com.example.newspaper.model.Article;
import com.example.newspaper.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ArticleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ArticleService articleService;

    @InjectMocks
    private ArticleController articleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(articleController).build();
    }

    @Test
    void shouldReturnAllArticles() throws Exception {
        // Подготовка данных для теста
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(1L);
        articleDto.setTitle("Title");
        articleDto.setContent("Content");
        articleDto.setPublishedDate(LocalDateTime.now());

        // Мокирование сервиса
        when(articleService.getRecentArticles()).thenReturn(Collections.singletonList(articleDto));

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$[0].content").value("Content"));
    }

    @Test
    void shouldCreateArticle() throws Exception {
        // Подготовка данных для теста
        Article article = new Article();
        article.setTitle("New Article");
        article.setContent("Article content");

        Article savedArticle = new Article();
        savedArticle.setId(1L);
        savedArticle.setTitle("New Article");
        savedArticle.setContent("Article content");
        savedArticle.setPublishedDate(LocalDateTime.now());

        // Мокирование сервиса
        when(articleService.saveArticle(any(Article.class))).thenReturn(savedArticle);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Article\",\"content\":\"Article content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Article"))
                .andExpect(jsonPath("$.content").value("Article content"));
    }

    @Test
    void shouldReturnBadRequestWhenTitleOrContentIsNull() throws Exception {
        // Выполнение запроса с ошибочными данными
        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":null,\"content\":\"Article content\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title and content are required"));

        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Article\",\"content\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Title and content are required"));
    }
}
