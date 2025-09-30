package com.joycart.backend.service;

import java.util.Map;

public interface CartService {
    

    Map<String, Integer> getCartItemCount(Integer userId, String productId);
}
