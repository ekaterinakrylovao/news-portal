package com.example.newspaper.service;

import com.example.newspaper.model.Article;
import com.example.newspaper.model.Like;
import com.example.newspaper.model.User;
import com.example.newspaper.repository.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void addLike(User user, Article article) {
        likeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .ifPresent(like -> {
                    throw new IllegalArgumentException("User already liked this article");
                });

        Like like = new Like();
        like.setUser(user);
        like.setArticle(article);
        likeRepository.save(like);
    }

    public void removeLike(User user, Article article) {
        likeRepository.findByUserIdAndArticleId(user.getId(), article.getId())
                .ifPresent(likeRepository::delete);
    }

    public int getLikesCount(Article article) {
        return likeRepository.countByArticleId(article.getId());
    }

    // Проверить, поставил ли пользователь лайк на статье
    public boolean isArticleLikedByUser(User user, Article article) {
        Optional<Like> like = likeRepository.findByUserIdAndArticleId(user.getId(), article.getId());
        return like.isPresent();
    }
}
