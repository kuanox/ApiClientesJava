package com.empresa.api.clientes.integration;

import com.empresa.api.clientes.dto.AuthResponse;
import com.empresa.api.clientes.dto.LoginRequest;
import com.empresa.api.clientes.dto.RegisterRequest;
import com.empresa.api.clientes.dto.UserDto;
import com.empresa.api.clientes.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UserRepository userRepository;
    
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
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
    }

    @Test
    void registerAndLogin_ShouldReturnToken() {
        // Register
        ResponseEntity<AuthResponse> registerResponse = restTemplate.postForEntity(
                "/api/v1/auth/register",
                registerRequest,
                AuthResponse.class);
        
        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
        assertNotNull(registerResponse.getBody().getToken());
        
        // Login
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "/api/v1/auth/login",
                loginRequest,
                AuthResponse.class);
        
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody().getToken());
    }

    @Test
    void getUser_ShouldReturnUser_WhenAuthenticated() {
        // Register
        ResponseEntity<AuthResponse> registerResponse = restTemplate.postForEntity(
                "/api/v1/auth/register",
                registerRequest,
                AuthResponse.class);
        
        String token = registerResponse.getBody().getToken();
        
        // Get user with token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        
        ResponseEntity<UserDto[]> usersResponse = restTemplate.exchange(
                "/api/v1/users",
                HttpMethod.GET,
                requestEntity,
                UserDto[].class);
        
        assertEquals(HttpStatus.OK, usersResponse.getStatusCode());
        assertTrue(usersResponse.getBody().length > 0);
    }

    @Test
    void getUser_ShouldReturnUnauthorized_WhenNotAuthenticated() {
        ResponseEntity<UserDto[]> response = restTemplate.getForEntity(
                "/api/v1/users",
                UserDto[].class);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}