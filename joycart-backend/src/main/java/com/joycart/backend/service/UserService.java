package com.joycart.backend.service;

import com.joycart.backend.dto.GoogleUserInfo;
import com.joycart.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public User saveUser(User user);
    public Optional<User> getUserById(int id);
    public Optional<User> getUserByPhoneNumber(String phoneNumber);
    public Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(int id, User user);
    void deleteUser(int id);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    boolean verifyPassword(String rawPassword, String encodedPassword);
    Optional<User> findByPhoneNumberOrEmail(String phoneNumberOrEmail);
    
    /**
     * 根据Google ID查找用户
     * 用于Google登录时查找已存在的用户
     * 
     * @param googleId Google用户唯一标识（sub字段）
     * @return 用户Optional对象，如果不存在返回empty
     */
    Optional<User> getUserByGoogleId(String googleId);

    /**
     * 根据Google用户信息创建或更新用户
     * 
     * @param googleUserInfo Google用户信息
     * @return 创建或更新后的用户，如果失败返回null
     */
    User createOrUpdateUserFromGoogle(GoogleUserInfo googleUserInfo);
}