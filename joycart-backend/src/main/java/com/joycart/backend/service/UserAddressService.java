package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface UserAddressService {
    
    /**
     * 根据用户ID获取所有激活的地址
     * @param userId 用户ID
     * @return 地址列表，转换为前端期望的Map格式
     */
    List<Map<String, Object>> getUserAddresses(Integer userId);
}
