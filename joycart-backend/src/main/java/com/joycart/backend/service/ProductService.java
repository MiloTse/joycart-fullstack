package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface ProductService {
    
    /**
     * 根据商品ID获取商品详情
     * @param productId 商品ID
     * @param languageCode 语言代码 (en-US, zh-CN, fr-FR)
     * @return 商品详情Map
     */
    Map<String, Object> getProductDetail(String productId, String languageCode);
    
    /**
     * 获取所有激活的商品列表
     * @return 商品列表
     */
    List<Map<String, Object>> getAllActiveProducts();
}
