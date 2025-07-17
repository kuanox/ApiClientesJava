package com.empresa.api.clientes.service;

import com.empresa.api.clientes.dto.UserDto;
import com.empresa.api.clientes.exeption.UserAlreadyExistsException;
import com.empresa.api.clientes.exeption.UserNotFoundException;
import com.empresa.api.clientes.model.Role;
import com.empresa.api.clientes.model.User;
import com.empresa.api.clientes.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks //MockBean se sa cuando usas @AutoConfigureMockMvc
    private UserService userService;
    
    private User user;
    
    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();
    }

    @Test
    void loadUserByUsername_ShouldReturnUser_WhenUserExists() {
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        
        var result = userService.loadUserByUsername(user.getEmail());
        
        assertEquals(user.getEmail(), result.getUsername());
        Mockito.verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
    void register_ShouldReturnUserDto_WhenUserNotExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        UserDto result = userService.register(user);
        
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void register_ShouldThrowException_WhenUserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(user));
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDto() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        
        List<UserDto> result = userService.getAllUsers();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        
        UserDto result = userService.getUserById(user.getId());
        
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDto_WhenUserExists() {
        User updatedUser = User.builder()
                .firstname("Updated")
                .lastname("Name")
                .email("updated@example.com")
                .build();
        
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        UserDto result = userService.updateUser(user.getId(), updatedUser);
        
        assertNotNull(result);
        assertEquals(updatedUser.getFirstname(), result.getFirstname());
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user.getId(), user));
        verify(userRepository, times(1)).findById(user.getId());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        
        userService.deleteUser(user.getId());
        
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotExists() {
        when(userRepository.existsById(user.getId())).thenReturn(false);
        
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getId()));
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, never()).deleteById(any());
    }
}