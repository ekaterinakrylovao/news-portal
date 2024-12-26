package com.example.newspaper.repository;

import com.example.newspaper.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndArticleId(Long userId, Long articleId);
    int countByArticleId(Long articleId);
}
