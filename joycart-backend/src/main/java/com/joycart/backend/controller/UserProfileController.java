package com.joycart.backend.controller;

import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    
    @Autowired
    private UserProfileService userProfileService;

    /**
     * 获取用户个人资料信息
     * @param userId 用户ID（从JWT token中获取）
     * @return 用户个人资料数据
     */
    @GetMapping("/profile")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getUserProfile(@RequestParam(required = false) String userId) {
        logger.info("Received user profile request for userId: {}", userId);
        
        try {
            // 如果没有提供userId，使用默认值（为了测试）
            if (userId == null || userId.isEmpty()) {
                userId = "12";
                logger.warn("No userId provided, using default userId: {}", userId);
            }
            
            // 从数据库获取用户资料
            Map<String, Object> profileData = userProfileService.getUserProfileByUserId(userId);
            
            if (profileData != null) {
                ResponseDTO<Map<String, Object>> response = ResponseDTO.success("用户资料获取成功", profileData);
                logger.info("User profile retrieved successfully from database for userId: {}", userId);
                return ResponseEntity.ok(response);
            } else {
                logger.warn("No user profile found for userId: {}, using default data", userId);
                // 如果数据库中没有数据，使用默认值
                Map<String, Object> defaultProfileData = new HashMap<>();
                defaultProfileData.put("id", userId);
                defaultProfileData.put("nickname", "Guest User");
                defaultProfileData.put("avatar", "/images/external/category-list-5.png");
                defaultProfileData.put("vipLevel", "VIP1");
                defaultProfileData.put("coupons", 0);
                defaultProfileData.put("rewardPoints", 0);
                defaultProfileData.put("phoneNumber", "");
                defaultProfileData.put("email", "");
                
                ResponseDTO<Map<String, Object>> response = ResponseDTO.success("用户资料获取成功（使用默认数据）", defaultProfileData);
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            logger.error("Error retrieving user profile for userId {}: {}", userId, e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error("获取用户资料失败，请重试");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
