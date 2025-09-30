package com.joycart.backend.service;

import java.util.Map;

public interface CartService {
    
    Map<String, Integer> getCartItemCount(Integer userId, String productId);
    
    Map<String, Object> addToCart(Integer userId, String productId, Integer count);
    
    Map<String, Object> changeCartItem(Integer userId, String productId, Integer count);
}
