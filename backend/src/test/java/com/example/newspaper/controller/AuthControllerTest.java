package com.example.newspaper.controller;

import com.example.newspaper.model.User;
import com.example.newspaper.security.JwtTokenUtil;
import com.example.newspaper.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        // Подготовка данных для теста
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        // Мокирование вызова сервиса
        doNothing().when(userService).registerUser(anyString(), anyString(), anyString(), anyString());

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        // Проверка, что метод registerUser был вызван
        verify(userService, times(1)).registerUser("John", "Doe", "john.doe@example.com", "password123");
    }

    @Test
    void shouldLoginUser() throws Exception {
        // Подготовка данных для теста
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        User existingUser = new User();
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setPassword("$2a$10$abcd1234efgh5678"); // Закодированный пароль

        // Мокирование вызова сервисов
        when(userService.findByEmail(user.getEmail())).thenReturn(existingUser);
        when(passwordEncoder.matches(user.getPassword(), existingUser.getPassword())).thenReturn(true);
        when(jwtTokenUtil.generateToken(user.getEmail())).thenReturn("generated_token");

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("Bearer generated_token"))
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
        // Подготовка данных для теста
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("wrongpassword");

        // Мокирование вызова сервисов
        when(userService.findByEmail(user.getEmail())).thenReturn(null);

        // Выполнение запроса с некорректными данными
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
