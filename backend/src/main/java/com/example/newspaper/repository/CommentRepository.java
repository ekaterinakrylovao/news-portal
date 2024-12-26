package com.example.newspaper.repository;

import com.example.newspaper.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdOrderByTimestampDesc(Long articleId);
}
