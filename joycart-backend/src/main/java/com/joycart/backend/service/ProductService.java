package com.joycart.backend.service;

import java.util.Map;
import java.util.Optional;

public interface ProductService {
    
    /**
     * 根据商品ID获取商品详情
     * @param productId 商品ID
     * @return 商品详情Map
     */
    Map<String, Object> getProductDetail(String productId);
}
