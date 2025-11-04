package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface HotSearchService {
    
    /**
     * 获取所有激活的热门搜索列表
     * @param languageCode 语言代码 (en-US, zh-CN, fr-FR)
     * @return 热门搜索列表
     */
    List<Map<String, String>> getAllActiveHotSearches(String languageCode);
    
    /**
     * 根据搜索ID获取热门搜索信息
     * @param searchId 搜索ID
     * @param languageCode 语言代码 (en-US, zh-CN, fr-FR)
     * @return 热门搜索信息
     */
    Map<String, String> getHotSearchById(String searchId, String languageCode);
    
    /**
     * 增加搜索次数
     * @param keyword 搜索关键词
     */
    void incrementSearchCount(String keyword);
}
