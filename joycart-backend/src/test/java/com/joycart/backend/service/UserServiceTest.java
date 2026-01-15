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

    /**
     * 测试根据Google ID查找用户 - 成功场景
     * 
     * 测试说明：
     * 1. 模拟Repository返回已存在的用户
     * 2. 验证Service方法返回正确的用户
     * 3. 验证Repository方法被正确调用
     * 
     * 注意：此测试需要User实体包含googleId字段（将在Task B9中添加）
     * 当前测试：验证方法调用和返回值处理逻辑
     */
    @Test
    void getUserByGoogleId_Success() {
        // Given: 准备测试数据
        String googleId = "12345678901234567890";
        User userWithGoogleId = new User();
        userWithGoogleId.setId(1);
        userWithGoogleId.setUsername("googleuser");
        userWithGoogleId.setEmail("google@example.com");
        // 注意：googleId字段将在Task B9中添加
        // userWithGoogleId.setGoogleId(googleId);
        
        // 模拟Repository返回用户
        when(userRepository.findByGoogleId(googleId)).thenReturn(Optional.of(userWithGoogleId));
        
        // When: 调用Service方法
        Optional<User> result = userService.getUserByGoogleId(googleId);
        
        // Then: 验证结果
        assertTrue(result.isPresent(), "应该找到用户");
        assertEquals(userWithGoogleId, result.get(), "返回的用户应该匹配");
        assertEquals(1, result.get().getId(), "用户ID应该正确");
        assertEquals("googleuser", result.get().getUsername(), "用户名应该正确");
        
        // 验证Repository方法被调用
        verify(userRepository, times(1)).findByGoogleId(googleId);
    }

    /**
     * 测试根据Google ID查找用户 - 用户不存在场景
     * 
     * 测试说明：
     * 1. 模拟Repository返回empty
     * 2. 验证Service方法返回empty
     * 3. 验证Repository方法被正确调用
     */
    @Test
    void getUserByGoogleId_NotFound() {
        // Given: 准备测试数据
        String googleId = "99999999999999999999";
        
        // 模拟Repository返回empty
        when(userRepository.findByGoogleId(googleId)).thenReturn(Optional.empty());
        
        // When: 调用Service方法
        Optional<User> result = userService.getUserByGoogleId(googleId);
        
        // Then: 验证结果
        assertFalse(result.isPresent(), "不应该找到用户");
        
        // 验证Repository方法被调用
        verify(userRepository, times(1)).findByGoogleId(googleId);
    }

    /**
     * 测试根据Google ID查找用户 - 空参数场景
     * 
     * 测试说明：
     * 1. 传入null或空字符串
     * 2. 验证方法返回empty，不调用Repository
     * 3. 验证参数验证逻辑
     */
    @Test
    void getUserByGoogleId_NullOrEmpty() {
        // Test 1: null参数
        Optional<User> result1 = userService.getUserByGoogleId(null);
        assertFalse(result1.isPresent(), "null参数应该返回empty");
        verify(userRepository, never()).findByGoogleId(anyString());
        
        // Test 2: 空字符串参数
        Optional<User> result2 = userService.getUserByGoogleId("");
        assertFalse(result2.isPresent(), "空字符串参数应该返回empty");
        verify(userRepository, never()).findByGoogleId("");
        
        // Test 3: 空白字符串参数
        Optional<User> result3 = userService.getUserByGoogleId("   ");
        assertFalse(result3.isPresent(), "空白字符串参数应该返回empty");
        verify(userRepository, never()).findByGoogleId("   ");
    }
}
