package com.joycart.backend.repository;

import com.joycart.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);
    
    /**
     * 根据Google ID查找用户
     * 注意：此方法需要User实体类包含googleId字段（将在Task B9中添加）
     * 
     * @param googleId Google用户唯一标识（sub字段）
     * @return 用户Optional对象，如果不存在返回empty
     */
    Optional<User> findByGoogleId(String googleId);
 }