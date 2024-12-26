package com.example.newspaper.service;

import com.example.newspaper.model.Comment;
import com.example.newspaper.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    public CommentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommentsByArticle() {
        Long articleId = 1L;
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");
        comments.add(comment);

        when(commentRepository.findByArticleIdOrderByTimestampDesc(articleId)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsByArticle(articleId);

        assertEquals(1, result.size());
        assertEquals("Test Comment", result.get(0).getContent());
        verify(commentRepository).findByArticleIdOrderByTimestampDesc(articleId);
    }

    @Test
    void testSaveComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");

        when(commentRepository.save(comment)).thenReturn(comment);

        Comment savedComment = commentService.saveComment(comment);

        assertEquals(1L, savedComment.getId());
        assertEquals("Test Comment", savedComment.getContent());
        verify(commentRepository).save(comment);
    }
}
