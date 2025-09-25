package com.joycart.backend.service;

import com.joycart.backend.model.User;
import com.joycart.backend.repository.UserRepository;
import com.joycart.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private User savedUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPhoneNumber("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setPassword("123456");

        savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("testuser");
        savedUser.setPhoneNumber("13800138000");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encoded_password");
    }

    @Test
    void saveUser_Success() {
        when(passwordEncoder.encode("123456")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.saveUser(testUser);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("13800138000", result.getPhoneNumber());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("encoded_password", result.getPassword());

        verify(passwordEncoder, times(1)).encode("123456");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void saveUser_WithException() {
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("Database connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.saveUser(testUser);
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(savedUser));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(savedUser, result.get());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void getUserById_NotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(999);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999);
    }




    @Test
    void getUserByPhoneNumber_Success() {
        when(userRepository.findByPhoneNumber("13800138000")).thenReturn(Optional.of(savedUser));

        Optional<User> result = userService.getUserByPhoneNumber("13800138000");

        assertTrue(result.isPresent());
        assertEquals(savedUser, result.get());
        verify(userRepository, times(1)).findByPhoneNumber("13800138000");
    }

    @Test
    void getUserByPhoneNumber_NotFound() {
        when(userRepository.findByPhoneNumber("13900139000")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByPhoneNumber("13900139000");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByPhoneNumber("13900139000");
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(savedUser));

        Optional<User> result = userService.getUserByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals(savedUser, result.get());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void getUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("notfound@example.com");

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    void getAllUsers_Success() {
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
        user2.setPhoneNumber("13900139000");
        user2.setPassword("password2");

        List<User> users = Arrays.asList(savedUser, user2);
        when(userRepository.findAll(any(Sort.class))).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(savedUser, result.get(0));
        assertEquals(user2, result.get(1));
        verify(userRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void updateUser_Success() {
        User updatedUser = new User();
        updatedUser.setUsername("updateduser");
        updatedUser.setPhoneNumber("13700137000");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");

        when(userRepository.findById(1)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.updateUser(1, updatedUser);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_UserNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(999, testUser);
        });

        verify(userRepository, times(1)).findById(999);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userRepository).deleteById(1);

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void existsByPhoneNumber_True() {
        when(userRepository.existsByPhoneNumber("13800138000")).thenReturn(true);

        boolean result = userService.existsByPhoneNumber("13800138000");

        assertTrue(result);
        verify(userRepository, times(1)).existsByPhoneNumber("13800138000");
    }

    @Test
    void existsByPhoneNumber_False() {
        when(userRepository.existsByPhoneNumber("13900139000")).thenReturn(false);

        boolean result = userService.existsByPhoneNumber("13900139000");

        assertFalse(result);
        verify(userRepository, times(1)).existsByPhoneNumber("13900139000");
    }

    @Test
    void existsByEmail_True() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean result = userService.existsByEmail("test@example.com");

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    void existsByEmail_False() {
        when(userRepository.existsByEmail("notfound@example.com")).thenReturn(false);

        boolean result = userService.existsByEmail("notfound@example.com");

        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail("notfound@example.com");
    }
}
