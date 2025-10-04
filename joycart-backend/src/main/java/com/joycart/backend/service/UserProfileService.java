package com.joycart.backend.service;

import java.util.Map;

public interface UserProfileService {
    
    /**
     * 根据用户ID获取用户资料
     * @param userId 用户ID
     * @return 用户资料信息
     */
    Map<String, Object> getUserProfileByUserId(String userId);
    
    /**
     * 创建或更新用户资料
     * @param userId 用户ID
     * @param profileData 用户资料数据
     * @return 操作结果
     */
    Map<String, Object> createOrUpdateUserProfile(String userId, Map<String, Object> profileData);
    
    /**
     * 检查用户资料是否存在
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean existsUserProfile(String userId);
}
