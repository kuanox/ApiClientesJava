package com.empresa.api.clientes.service;

import com.empresa.api.clientes.dto.UserDto;
import com.empresa.api.clientes.exeption.UserAlreadyExistsException;
import com.empresa.api.clientes.exeption.UserNotFoundException;
import com.empresa.api.clientes.model.User;
import com.empresa.api.clientes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public UserDto register(User user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }
        
        User savedUser = repository.save(user);
        return convertToDto(savedUser);
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Integer id) {
        return repository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public UserDto updateUser(Integer id, User userDetails) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        
        user.setFirstname(userDetails.getFirstname());
        user.setLastname(userDetails.getLastname());
        user.setEmail(userDetails.getEmail());
        
        User updatedUser = repository.save(user);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        repository.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }

}