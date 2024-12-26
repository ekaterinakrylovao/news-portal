package com.example.newspaper.service;

import com.example.newspaper.model.Comment;
import com.example.newspaper.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByArticle(Long articleId) {
        return commentRepository.findByArticleIdOrderByTimestampDesc(articleId);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
