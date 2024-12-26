package com.example.newspaper.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    // Генерация JWT токена
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration)).
                signWith(key())
                .compact();
    }

    // Получение email из токена
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Проверка, не истек ли токен
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Получение даты истечения срока действия токена
    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    // Проверка токена на валидность
    public boolean validateToken(String token, String email) {
        return (email.equals(getUsernameFromToken(token)) && !isTokenExpired(token));
    }
}
