package com.example.newspaper.repository;

import com.example.newspaper.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByPublishedDateAfterOrderByPublishedDateDesc(LocalDateTime dateTime);
}
