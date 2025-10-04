package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface LocationService {
    
    /**
     * 获取所有激活的位置列表
     * @return 位置列表
     */
    List<Map<String, String>> getAllActiveLocations();
    
    /**
     * 根据位置ID获取位置信息
     * @param locationId 位置ID
     * @return 位置信息
     */
    Map<String, String> getLocationById(String locationId);
}
