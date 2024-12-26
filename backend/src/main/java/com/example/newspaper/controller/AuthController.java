package com.example.newspaper.controller;

import com.example.newspaper.dtos.UserDto;
import com.example.newspaper.model.User;
import com.example.newspaper.security.JwtTokenUtil;
import com.example.newspaper.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;  // Добавляем зависимость

    public AuthController(UserService userService, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;  // Инициализируем зависимость
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            userService.registerUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Проверка существования пользователя
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Проверка пароля
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Генерация токена
        String token = jwtTokenUtil.generateToken(existingUser.getEmail());

        // Формируем ответ, включая токен и данные пользователя
        Map<String, Object> response = new HashMap<>();
        response.put("token", "Bearer " + token);
        response.put("user", new UserDto(existingUser.getFirstName(), existingUser.getLastName())); // Пример DTO для пользователя

        return ResponseEntity.ok(response);
    }

    // Обработка ошибки BadCredentialsException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
