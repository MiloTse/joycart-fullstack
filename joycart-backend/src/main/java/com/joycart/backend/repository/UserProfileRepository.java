package com.joycart.backend.repository;

import com.joycart.backend.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    
    /**
     * 根据用户ID查找激活的用户资料
     */
    Optional<UserProfile> findByUserIdAndIsActiveTrue(String userId);
    
    /**
     * 根据用户ID查找用户资料（包括非激活的）
     */
    Optional<UserProfile> findByUserId(String userId);
    
    /**
     * 检查用户资料是否存在
     */
    boolean existsByUserId(String userId);
}
