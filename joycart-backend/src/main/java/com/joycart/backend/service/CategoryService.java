package com.joycart.backend.service;

import java.util.Map;

public interface CategoryService {
    
    /**
     * 获取分类和标签列表
     * @return 包含分类和标签的Map
     */
    Map<String, Object> getCategoryAndTagList();
    
}
