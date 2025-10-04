package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface BannerService {
    
    /**
     * 获取所有激活的轮播图列表
     * @return 轮播图列表
     */
    List<Map<String, String>> getAllActiveBanners();
    
    /**
     * 根据轮播图ID获取轮播图信息
     * @param bannerId 轮播图ID
     * @return 轮播图信息
     */
    Map<String, String> getBannerById(String bannerId);
}
