package com.example.newspaper.controller;

import com.example.newspaper.dtos.ArticleDto;
import com.example.newspaper.model.Article;
import com.example.newspaper.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        List<ArticleDto> articles = articleService.getRecentArticles();
        return ResponseEntity.ok(articles);
    }

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody Article article) {
        if (article.getTitle() == null || article.getContent() == null) {
            return ResponseEntity.badRequest().body("Title and content are required");
        }
        article.setPublishedDate(LocalDateTime.now());
        Article savedArticle = articleService.saveArticle(article);
        return ResponseEntity.ok(savedArticle);
    }
}
