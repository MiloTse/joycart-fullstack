package com.joycart.backend.service.impl;

import com.joycart.backend.dto.GoogleUserInfo;
import com.joycart.backend.model.User;
import com.joycart.backend.repository.UserRepository;
import com.joycart.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
    
    /**
     * 验证用户密码
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * 根据手机号或邮箱查找用户
     * @param phoneNumberOrEmail 手机号或邮箱
     * @return 用户信息
     */
    public Optional<User> findByPhoneNumberOrEmail(String phoneNumberOrEmail) {
        //先尝试按手机号查找
        Optional<User> userByPhone = userRepository.findByPhoneNumber(phoneNumberOrEmail);
        if (userByPhone.isPresent()) {
            return userByPhone;
        }
        
        //再尝试按邮箱查找
        return userRepository.findByEmail(phoneNumberOrEmail);
    }

    /**
     * 根据Google ID查找用户
     * 用于Google登录时查找已存在的用户
     * 
     * 注意：此方法需要User实体类包含googleId字段（将在Task B9中添加）
     * 当前实现：调用Repository方法，添加日志输出以便调试
     * 
     * @param googleId Google用户唯一标识（sub字段）
     * @return 用户Optional对象，如果不存在返回empty
     */
    @Override
    public Optional<User> getUserByGoogleId(String googleId) {
        logger.debug("Attempting to find user by Google ID: {}", 
                googleId != null && googleId.length() > 10 
                    ? googleId.substring(0, 10) + "..." : googleId);
        
        if (googleId == null || googleId.trim().isEmpty()) {
            logger.warn("Google ID is null or empty - cannot find user");
            return Optional.empty();
        }
        
        try {
            Optional<User> user = userRepository.findByGoogleId(googleId);
            
            if (user.isPresent()) {
                logger.info("User found by Google ID: {} - User ID: {}, Username: {}", 
                        googleId.substring(0, Math.min(10, googleId.length())), 
                        user.get().getId(), 
                        user.get().getUsername());
            } else {
                logger.debug("No user found with Google ID: {}", 
                        googleId.substring(0, Math.min(10, googleId.length())));
            }
            
            return user;
        } catch (Exception e) {
            logger.error("Error finding user by Google ID: {} - {}", 
                    googleId != null && googleId.length() > 10 
                        ? googleId.substring(0, 10) + "..." : googleId, 
                    e.getMessage(), e);
            // 注意：如果User实体还没有googleId字段，这里会抛出异常
            // 这是预期的，将在Task B9添加字段后解决
            throw e;
        }
    }

    /**
     * 根据Google用户信息创建或更新用户
     * 
     * 注意：当前User实体尚未包含googleId字段（将在Task B9中添加）
     * 这里优先使用email查找用户，避免googleId字段缺失导致运行时异常
     * 
     * @param googleUserInfo Google用户信息
     * @return 创建或更新后的用户，如果失败返回null
     */
    @Override
    public User createOrUpdateUserFromGoogle(GoogleUserInfo googleUserInfo) {
        if (googleUserInfo == null) {
            logger.warn("GoogleUserInfo is null - cannot create or update user");
            return null;
        }

        String email = googleUserInfo.getEmail();
        String googleId = googleUserInfo.getGoogleId();
        String name = googleUserInfo.getName();

        logger.info("Processing Google user info - email: {}, hasGoogleId: {}",
                email, googleId != null && !googleId.trim().isEmpty());

        if ((email == null || email.trim().isEmpty()) 
                && (googleId == null || googleId.trim().isEmpty())) {
            logger.warn("GoogleUserInfo missing email and googleId - cannot identify user");
            return null;
        }

        Optional<User> existingUser = Optional.empty();

        if (googleId != null && !googleId.trim().isEmpty()) {
            try {
                existingUser = getUserByGoogleId(googleId);
            } catch (Exception e) {
                logger.warn("Failed to lookup user by Google ID - falling back to email lookup: {}", e.getMessage());
            }
        }

        if (!existingUser.isPresent() && email != null && !email.trim().isEmpty()) {
            existingUser = getUserByEmail(email);
        }

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            boolean updated = false;

            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                String fallbackUsername = (name != null && !name.trim().isEmpty())
                        ? name
                        : (email != null && !email.trim().isEmpty() ? email : "google_user");
                user.setUsername(fallbackUsername);
                updated = true;
            }

            if ((user.getEmail() == null || user.getEmail().trim().isEmpty()) 
                    && email != null && !email.trim().isEmpty()) {
                user.setEmail(email);
                updated = true;
            }

            if (user.getIsActive() == null) {
                user.setIsActive(true);
                updated = true;
            }

            if (updated) {
                User updatedUser = userRepository.save(user);
                logger.info("Updated existing Google user: id={}, email={}", 
                        updatedUser.getId(), updatedUser.getEmail());
                return updatedUser;
            }

            logger.info("Existing Google user found - no update required: id={}, email={}", 
                    user.getId(), user.getEmail());
            return user;
        }

        User newUser = new User();
        String username = (name != null && !name.trim().isEmpty())
                ? name
                : (email != null && !email.trim().isEmpty() ? email : "google_user");
        newUser.setUsername(username);
        newUser.setEmail(email);

        String seed = (googleId != null && !googleId.trim().isEmpty()) ? googleId : email;
        if (seed == null || seed.trim().isEmpty()) {
            seed = UUID.randomUUID().toString();
        }
        long hash = Math.abs(seed.hashCode());
        String phoneNumber = "9" + String.format("%09d", hash % 1_000_000_000L);
        newUser.setPhoneNumber(phoneNumber);

        String rawPassword = UUID.randomUUID().toString();
        newUser.setPassword(rawPassword);

        User savedUser = saveUser(newUser);
        logger.info("Created new user from Google login: id={}, email={}", 
                savedUser.getId(), savedUser.getEmail());
        return savedUser;
    }
}
