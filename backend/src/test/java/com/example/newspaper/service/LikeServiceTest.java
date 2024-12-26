package com.example.newspaper.service;

import com.example.newspaper.model.Article;
import com.example.newspaper.model.Like;
import com.example.newspaper.model.User;
import com.example.newspaper.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeService likeService;

    public LikeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddLike() {
        User user = new User();
        user.setId(1L);
        Article article = new Article();
        article.setId(1L);

        when(likeRepository.findByUserIdAndArticleId(user.getId(), article.getId()))
                .thenReturn(Optional.empty());

        likeService.addLike(user, article);

        verify(likeRepository).save(any(Like.class));
    }

    @Test
    void testRemoveLike() {
        User user = new User();
        user.setId(1L);
        Article article = new Article();
        article.setId(1L);
        Like like = new Like();
        like.setUser(user);
        like.setArticle(article);

        when(likeRepository.findByUserIdAndArticleId(user.getId(), article.getId()))
                .thenReturn(Optional.of(like));

        likeService.removeLike(user, article);

        verify(likeRepository).delete(like);
    }

    @Test
    void testGetLikesCount() {
        Article article = new Article();
        article.setId(1L);

        when(likeRepository.countByArticleId(article.getId())).thenReturn(5);

        int count = likeService.getLikesCount(article);

        assertEquals(5, count);
        verify(likeRepository).countByArticleId(article.getId());
    }

    @Test
    void testIsArticleLikedByUser() {
        User user = new User();
        user.setId(1L);
        Article article = new Article();
        article.setId(1L);

        when(likeRepository.findByUserIdAndArticleId(user.getId(), article.getId()))
                .thenReturn(Optional.of(new Like()));

        boolean result = likeService.isArticleLikedByUser(user, article);

        assertTrue(result);
    }
}
