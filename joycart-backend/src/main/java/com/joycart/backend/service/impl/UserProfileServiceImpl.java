package com.joycart.backend.service.impl;

import com.joycart.backend.model.UserProfile;
import com.joycart.backend.repository.UserProfileRepository;
import com.joycart.backend.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Override
    public Map<String, Object> getUserProfileByUserId(String userId) {
        logger.debug("Getting user profile for userId: {}", userId);
        
        try {
            Optional<UserProfile> profileOptional = userProfileRepository.findByUserIdAndIsActiveTrue(userId);
            
            if (profileOptional.isPresent()) {
                UserProfile profile = profileOptional.get();
                
                Map<String, Object> profileData = new HashMap<>();
                profileData.put("id", profile.getUserId());
                profileData.put("nickname", profile.getNickname());
                profileData.put("avatar", profile.getAvatar());
                profileData.put("vipLevel", profile.getVipLevel());
                profileData.put("coupons", profile.getCoupons());
                profileData.put("rewardPoints", profile.getRewardPoints());
                profileData.put("phoneNumber", profile.getPhoneNumber());
                profileData.put("email", profile.getEmail());
                
                logger.info("User profile retrieved successfully for userId: {}", userId);
                return profileData;
                
            } else {
                logger.warn("No user profile found for userId: {}", userId);
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Error retrieving user profile for userId {}: {}", userId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    @Transactional
    public Map<String, Object> createOrUpdateUserProfile(String userId, Map<String, Object> profileData) {
        logger.debug("Creating or updating user profile for userId: {}", userId);
        
        try {
            Optional<UserProfile> existingProfile = userProfileRepository.findByUserId(userId);
            UserProfile profile;
            
            if (existingProfile.isPresent()) {
                profile = existingProfile.get();
                logger.info("Updating existing user profile for userId: {}", userId);
            } else {
                profile = new UserProfile();
                profile.setUserId(userId);
                logger.info("Creating new user profile for userId: {}", userId);
            }
            
            // 更新用户资料字段
            if (profileData.containsKey("nickname")) {
                profile.setNickname((String) profileData.get("nickname"));
            }
            if (profileData.containsKey("avatar")) {
                profile.setAvatar((String) profileData.get("avatar"));
            }
            if (profileData.containsKey("vipLevel")) {
                profile.setVipLevel((String) profileData.get("vipLevel"));
            }
            if (profileData.containsKey("coupons")) {
                profile.setCoupons((Integer) profileData.get("coupons"));
            }
            if (profileData.containsKey("rewardPoints")) {
                profile.setRewardPoints((Integer) profileData.get("rewardPoints"));
            }
            if (profileData.containsKey("phoneNumber")) {
                profile.setPhoneNumber((String) profileData.get("phoneNumber"));
            }
            if (profileData.containsKey("email")) {
                profile.setEmail((String) profileData.get("email"));
            }
            
            profile.setIsActive(true);
            userProfileRepository.save(profile);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "用户资料保存成功");
            result.put("userId", userId);
            
            logger.info("User profile saved successfully for userId: {}", userId);
            return result;
            
        } catch (Exception e) {
            logger.error("Error saving user profile for userId {}: {}", userId, e.getMessage(), e);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "用户资料保存失败: " + e.getMessage());
            return result;
        }
    }
    
    @Override
    public boolean existsUserProfile(String userId) {
        logger.debug("Checking if user profile exists for userId: {}", userId);
        
        try {
            boolean exists = userProfileRepository.existsByUserId(userId);
            logger.info("User profile exists check for userId {}: {}", userId, exists);
            return exists;
            
        } catch (Exception e) {
            logger.error("Error checking user profile existence for userId {}: {}", userId, e.getMessage(), e);
            return false;
        }
    }
}
