package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    /**
     * 获取用户个人资料信息
     * @return 用户个人资料数据
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        logger.info("Received user profile request");
        
        try {
            //暂时硬编码用户资料数据
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("id", "12");
            profileData.put("nickname", "Tom Wang");
            profileData.put("avatar", "/images/external/category-list-5.png");
            profileData.put("vipLevel", "VIP5");
            profileData.put("coupons", 4);
            profileData.put("rewardPoints", 258);
            profileData.put("phoneNumber", "1234567890");
            profileData.put("email", "tom.wang@example.com");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "success");
            response.put("data", profileData);
            
            logger.info("User profile retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving user profile: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "error");
            errorResponse.put("data", null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
