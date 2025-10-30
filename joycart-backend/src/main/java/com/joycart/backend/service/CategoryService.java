package com.joycart.backend.service;

import java.util.Map;

public interface CategoryService {
    
    /**
     * 获取分类和标签列表
     * @param languageCode 语言代码 (en-US, zh-CN, fr-FR)
     * @return 包含分类和标签的Map
     */
    Map<String, Object> getCategoryAndTagList(String languageCode);
    
}
