package com.joycart.backend.controller;

import com.joycart.backend.dto.ResponseDTO;
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
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getUserProfile() {
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
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success("用户资料获取成功", profileData);
            
            logger.info("User profile retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving user profile: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error("获取用户资料失败，请重试");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
