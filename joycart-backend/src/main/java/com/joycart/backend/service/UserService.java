package com.joycart.backend.service;

import com.joycart.backend.model.User;
import com.joycart.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        logger.debug("Attempting to save user: {}", user.getUsername());
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            LocalDateTime now = LocalDateTime.now();
            user.setCreatedAt(now);
            user.setUpdatedAt(now);
            user.setIsActive(true);
            
            User savedUser = userRepository.save(user);
            logger.info("User saved successfully with ID: {} for username: {}", 
                       savedUser.getId(), savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error saving user: {} - {}", user.getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }



    @Override
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public User updateUser(int id, User user) {
        User userToUpdate = userRepository.findById(id).orElseThrow();
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPhoneNumber(user.getPhoneNumber());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setPassword(user.getPassword());
        return userRepository.save(userToUpdate);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Optional<User> findByPhoneNumberOrEmail(String phoneNumberOrEmail) {
        Optional<User> userByPhone = userRepository.findByPhoneNumber(phoneNumberOrEmail);
        if (userByPhone.isPresent()) {
            return userByPhone;
        }
        return userRepository.findByEmail(phoneNumberOrEmail);
    }
}