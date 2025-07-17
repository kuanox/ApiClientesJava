package com.empresa.api.clientes.controller;

import com.empresa.api.clientes.config.JwtService;
import com.empresa.api.clientes.dto.AuthResponse;
import com.empresa.api.clientes.dto.LoginRequest;
import com.empresa.api.clientes.dto.RegisterRequest;
import com.empresa.api.clientes.dto.UserDto;
import com.empresa.api.clientes.model.Role;
import com.empresa.api.clientes.model.User;
import com.empresa.api.clientes.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private UserService userService;
    
    @Mock
    private JwtService jwtService;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @InjectMocks
    private AuthController authController;
    
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    
    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();
        
        loginRequest = LoginRequest.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();
        
        user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenValidRequest() {
        when(userService.register(any(User.class))).thenReturn(new UserDto());
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        
        ResponseEntity<AuthResponse> response = authController.register(registerRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token", response.getBody().getToken());
        verify(userService, times(1)).register(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenValidRequest() {
        when(userService.loadUserByUsername(loginRequest.getEmail())).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token", response.getBody().getToken());
        verify(userService, times(1)).loadUserByUsername(loginRequest.getEmail());
        verify(jwtService, times(1)).generateToken(any(User.class));
    }
}