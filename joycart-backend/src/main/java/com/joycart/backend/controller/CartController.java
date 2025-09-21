package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    /**
     * 获取商品在购物车中的数量
     * @param id 商品ID
     * @return 购物车中该商品的数量
     */
    @GetMapping("/item")
    public ResponseEntity<?> getCartItemCount(@RequestParam String id) {
        logger.info("Received cart item count request for productId: {}", id);
        
        try {
            // 硬编码购物车数量（实际项目中应该从数据库根据用户ID和商品ID查询）
            int count = 18; // 模拟该商品在购物车中的数量
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            Map<String, Integer> data = new HashMap<>();
            data.put("count", count);
            response.put("data", data);
            
            logger.info("Cart item count retrieved successfully for productId: {}, count: {}", id, count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving cart item count: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("data", Map.of("count", 0));
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 更新购物车中商品的数量
     * @param id 商品ID
     * @param count 新的数量
     * @return 更新结果
     */
    @PostMapping("/change")
    public ResponseEntity<?> changeCartItem(
            @RequestParam String id,
            @RequestParam int count) {
        
        logger.info("Received cart change request for productId: {}, count: {}", id, count);
        
        try {
            // 硬编码更新结果（实际项目中应该更新数据库）
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cart updated successfully");
            
            Map<String, Object> data = new HashMap<>();
            data.put("id", Integer.parseInt(id));
            data.put("count", count);
            data.put("updatedAt", LocalDateTime.now().toString());
            response.put("data", data);
            
            logger.info("Cart item updated successfully for productId: {}, new count: {}", id, count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error updating cart item: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update cart");
            errorResponse.put("data", null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}