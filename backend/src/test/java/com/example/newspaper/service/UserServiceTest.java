package com.example.newspaper.service;

import com.example.newspaper.model.User;
import com.example.newspaper.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        userService.registerUser("John", "Doe", email, password);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail(email);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals(email, foundUser.getEmail());
    }
}
