package com.joycart.backend.service;

import com.joycart.backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    public User saveUser(User user);
    public Optional<User> getUserById(int id);
     public Optional<User> getUserByPhoneNumber(String phoneNumber);
    public Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    User updateUser(int id, User user);
    void deleteUser(int id);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
}