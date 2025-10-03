package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface NearbyStoreService {
    
    /**
     * 获取所有激活的附近商店
     * @return 商店列表，转换为前端期望的Map格式
     */
    List<Map<String, String>> getAllActiveStores();
}
