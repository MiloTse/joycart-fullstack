package com.joycart.backend.service.impl;

import com.joycart.backend.model.Cart;
import com.joycart.backend.repository.CartRepository;
import com.joycart.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Map<String, Integer> getCartItemCount(Integer userId, String productId) {
        logger.debug("Getting cart item count for userId: {}, productId: {}", userId, productId);
        
        try {
            Optional<Cart> cartOptional = cartRepository.findByUserIdAndProductIdAndIsActiveTrue(userId, productId);
            
            int count = 0;
            if (cartOptional.isPresent()) {
                count = cartOptional.get().getQuantity();
                logger.debug("Found cart item for userId: {}, productId: {}, count: {}", userId, productId, count);
            } else {
                logger.debug("No cart item found for userId: {}, productId: {}", userId, productId);
            }
            
            Map<String, Integer> result = new HashMap<>();
            result.put("count", count);
            
            logger.info("Cart item count retrieved successfully for userId: {}, productId: {}, count: {}", userId, productId, count);
            return result;
            
        } catch (Exception e) {
            logger.error("Error getting cart item count for userId: {}, productId: {} - {}", userId, productId, e.getMessage(), e);
            // 发生异常时返回0
            Map<String, Integer> result = new HashMap<>();
            result.put("count", 0);
            return result;
        }
    }
}
