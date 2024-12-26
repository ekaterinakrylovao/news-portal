package com.example.newspaper.service;

import com.example.newspaper.dtos.ArticleDto;
import com.example.newspaper.model.Article;
import com.example.newspaper.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void updateArticleImage(Long articleId, String imageUrl) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new IllegalArgumentException("Статья с id " + articleId + " не найдена")
        );
        article.setImageUrl(imageUrl);
        articleRepository.save(article);
    }

    public List<ArticleDto> getRecentArticles() {
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        List<Article> articles = articleRepository.findByPublishedDateAfterOrderByPublishedDateDesc(last24Hours);
        return articles.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    public Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElse(null);
    }

    private ArticleDto convertToDTO(Article article) {
        ArticleDto dto = new ArticleDto();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setImageUrl(article.getImageUrl());
        dto.setPublishedDate(article.getPublishedDate());
        return dto;
    }
}
