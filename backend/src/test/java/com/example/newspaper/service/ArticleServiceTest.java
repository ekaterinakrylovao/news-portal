package com.example.newspaper.service;

import com.example.newspaper.dtos.ArticleDto;
import com.example.newspaper.model.Article;
import com.example.newspaper.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    public ArticleServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateArticleImage() {
        Article article = new Article();
        article.setId(1L);
        article.setImageUrl("old_url.jpg");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        articleService.updateArticleImage(1L, "new_url.jpg");

        assertEquals("new_url.jpg", article.getImageUrl());
        verify(articleRepository).save(article);
    }

    @Test
    void testGetRecentArticles() {
        List<Article> articles = new ArrayList<>();
        Article article = new Article();
        article.setId(1L);
        article.setTitle("Test Article");
        article.setPublishedDate(LocalDateTime.now());
        articles.add(article);

        when(articleRepository.findByPublishedDateAfterOrderByPublishedDateDesc(any(LocalDateTime.class)))
                .thenReturn(articles);

        List<ArticleDto> result = articleService.getRecentArticles();

        assertEquals(1, result.size());
        assertEquals("Test Article", result.get(0).getTitle());
    }

    @Test
    void testSaveArticle() {
        Article article = new Article();
        article.setId(1L);

        when(articleRepository.save(article)).thenReturn(article);

        Article savedArticle = articleService.saveArticle(article);

        assertEquals(1L, savedArticle.getId());
        verify(articleRepository).save(article);
    }

    @Test
    void testGetArticleById() {
        Article article = new Article();
        article.setId(1L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));

        Article foundArticle = articleService.getArticleById(1L);

        assertNotNull(foundArticle);
        assertEquals(1L, foundArticle.getId());
    }
}
