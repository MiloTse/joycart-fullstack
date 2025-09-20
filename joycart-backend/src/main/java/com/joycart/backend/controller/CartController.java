package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/products")
    public ResponseEntity<?> getCartProducts() {
        logger.info("Received cart products request");
        
        try {
            // 硬编码购物车数据（模拟原始JSON）
            List<Map<String, Object>> cartData = Arrays.asList(
                createMockShop("8137", "Ximei Vegetable Store", Arrays.asList(
                    createMockCartProduct("88391", "/images/external/category-list-5.png", "0.45kg", 
                        "Weifang Fruit Radish 10kg Sweet Crisp Fruit-type Tianjin Radish", 14.9, 2),
                    createMockCartProduct("88392", "/images/external/category-list-3.png", "0.45kg",
                        "City Kitchen Australian Beef Roll 450g Hot Pot Fresh Beef & Lamb Ingredients", 35.0, 2)
                )),
                createMockShop("8318", "Premium Fruit Market", Arrays.asList(
                    createMockCartProduct("89391", "/images/external/category-list-6.png", "0.9kg",
                        "Premium Fresh Live Fish Three-Cleaned Red Star Grouper Rainbow Tilapia 900g", 69.9, 1),
                    createMockCartProduct("89392", "/images/external/category-list-2.png", "1.5kg",
                        "Ximei Domestic Large Shrimp 1.8kg 90-108pcs/box BBQ Hot Pot Fresh Seafood...", 119.0, 3)
                ))
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", cartData);
            
            logger.info("Cart products retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving cart products: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("data", Arrays.asList());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    private Map<String, Object> createMockShop(String shopId, String shopName, List<Map<String, Object>> cartList) {
        Map<String, Object> shop = new HashMap<>();
        shop.put("shopId", shopId);
        shop.put("shopName", shopName);
        shop.put("cartList", cartList);
        return shop;
    }
    
    private Map<String, Object> createMockCartProduct(String productId, String imgUrl, String weight, 
                                                     String title, double price, int count) {
        Map<String, Object> product = new HashMap<>();
        product.put("productId", productId);
        product.put("imgUrl", imgUrl);
        product.put("weight", weight);
        product.put("title", title);
        product.put("price", price);
        product.put("count", count);
        return product;
    }
}
