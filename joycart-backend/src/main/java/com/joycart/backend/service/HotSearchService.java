package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface HotSearchService {
    
    /**
     * 获取所有激活的热门搜索列表
     * @return 热门搜索列表
     */
    List<Map<String, String>> getAllActiveHotSearches();
    
    /**
     * 根据搜索ID获取热门搜索信息
     * @param searchId 搜索ID
     * @return 热门搜索信息
     */
    Map<String, String> getHotSearchById(String searchId);
    
    /**
     * 增加搜索次数
     * @param keyword 搜索关键词
     */
    void incrementSearchCount(String keyword);
}
