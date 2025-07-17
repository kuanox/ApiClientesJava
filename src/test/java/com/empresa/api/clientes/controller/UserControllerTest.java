package com.empresa.api.clientes.controller;

import com.empresa.api.clientes.dto.UserDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    
    @InjectMocks
    private UserController userController;
    
    private UserDto userDto;
    private User user;
    
    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .build();
        
        user = User.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .build();
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));
        
        ResponseEntity<List<UserDto>> response = userController.getAllUsers();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userService.getUserById(1)).thenReturn(userDto);
        
        ResponseEntity<UserDto> response = userController.getUserById(1);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDto.getEmail(), response.getBody().getEmail());
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() {
        when(userService.updateUser(1, user)).thenReturn(userDto);
        
        ResponseEntity<UserDto> response = userController.updateUser(1, user);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userDto.getEmail(), response.getBody().getEmail());
        verify(userService, times(1)).updateUser(1, user);
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() {
        doNothing().when(userService).deleteUser(1);
        
        ResponseEntity<Void> response = userController.deleteUser(1);
        
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1);
    }
}