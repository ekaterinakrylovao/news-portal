package com.example.newspaper.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "likes")  // Указываем имя таблицы
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "like_seq")
    @SequenceGenerator(name = "like_seq", sequenceName = "like_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @JsonBackReference  // Предотвращает рекурсию при сериализации
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore  // Скроем объект пользователя в ответе, чтобы избежать избыточной информации
    private User user;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
