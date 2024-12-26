package com.example.newspaper.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtTokenUtil = new JwtTokenUtil();

        // Используем рефлексию для изменения приватных полей
        Field secretKeyField = JwtTokenUtil.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenUtil, "mySecretKey123912738fdjskfhdhksjdkfhdlkfldhlfhakfkjh");

        Field expirationField = JwtTokenUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtTokenUtil, 1000 * 60 * 60); // 1 час
    }

    @Test
    void generateToken_shouldGenerateToken() {
        String email = "test@example.com";
        String token = jwtTokenUtil.generateToken(email);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void getUsernameFromToken_shouldReturnUsername() {
        String email = "test@example.com";
        String token = jwtTokenUtil.generateToken(email);

        String username = jwtTokenUtil.getUsernameFromToken(token);

        assertEquals(email, username);
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String email = "test@example.com";
        String token = jwtTokenUtil.generateToken(email);

        boolean isValid = jwtTokenUtil.validateToken(token, email);

        assertTrue(isValid);
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        String email = "test@example.com";
        String token = jwtTokenUtil.generateToken(email);
        String invalidEmail = "invalid@example.com";

        boolean isValid = jwtTokenUtil.validateToken(token, invalidEmail);

        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_shouldReturnFalseForValidToken() {
        String email = "test@example.com";
        String token = jwtTokenUtil.generateToken(email);

        // Здесь просто проверяем, что токен не истек
        boolean isExpired = jwtTokenUtil.isTokenExpired(token);

        assertFalse(isExpired); // Токен не должен быть истекшим сразу
    }
}
