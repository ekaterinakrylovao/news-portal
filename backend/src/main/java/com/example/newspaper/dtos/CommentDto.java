package com.example.newspaper.dtos;

public class CommentDto {
    private Long id;
    private String content;
    private String timestamp;
    private String username;

    // Конструктор
    public CommentDto(Long id, String content, String timestamp, String username) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.username = username;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }
}
