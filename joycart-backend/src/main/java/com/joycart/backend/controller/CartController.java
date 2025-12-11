package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.CartService;
import com.joycart.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 获取购物车商品列表
     * @param token JWT token (从Header中获取)
     * @return 购物车中所有商品列表
     */
    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<Object[]>> getCartProducts(
            @RequestHeader(value = ApiConstants.AUTHORIZATION_HEADER, required = false) String token) {
        logger.info("Received cart products request");
        
        try {
            // 从JWT token中获取用户ID
            Integer userId = null;
            if (token != null && token.startsWith(ApiConstants.BEARER_PREFIX)) {
                try {
                    String jwtToken = token.substring(7);
                    userId = jwtUtil.getUserIdFromToken(jwtToken);
                    logger.debug("Extracted userId: {} from token", userId);
                } catch (Exception e) {
                    logger.warn("Failed to extract userId from token: {}", e.getMessage());
                }
            }
            
            // 如果没有有效的用户ID，使用默认值1（保持向后兼容）
            if (userId == null) {
                userId = 1;
                logger.debug("Using default userId: {}", userId);
            }
            
            // 调用CartService获取购物车商品
            Object[] cartData = cartService.getCartProducts(userId);
            
            ResponseDTO<Object[]> response = ResponseDTO.success(ApiConstants.CART_PRODUCTS_SUCCESS_MESSAGE, cartData);
            
            logger.info("Cart products retrieved successfully for userId: {}, found {} shops", userId, cartData.length);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving cart products: {}", e.getMessage(), e);
            ResponseDTO<Object[]> errorResponse = ResponseDTO.error(ApiConstants.CART_PRODUCTS_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 获取商品的购物车信息（用于Category页面弹窗）
     * @param productId 商品ID
     * @return 商品的购物车信息
     */
    @GetMapping("/product-info")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getCartProductInfo(@RequestParam String productId) {
        logger.info("Received cart product info request for productId: {}", productId);
        
        try {
            // 调用CartService获取商品购物车信息
            Map<String, Object> productInfo = cartService.getCartProductInfo(productId);
            
            if (productInfo == null) {
                logger.warn("Product not found for productId: {}", productId);
                ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(ApiConstants.PRODUCT_NOT_FOUND_MESSAGE);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success(ApiConstants.CART_PRODUCT_INFO_SUCCESS_MESSAGE, productInfo);
            
            logger.info("Cart product info retrieved successfully for productId: {}", productId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving cart product info: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(ApiConstants.CART_PRODUCT_INFO_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 获取商品在购物车中的数量
     * @param id 商品ID
     * @param token JWT token (从Header中获取)
     * @return 购物车中该商品的数量
     */
    @GetMapping("/item")
    public ResponseEntity<ResponseDTO<Map<String, Integer>>> getCartItemCount(
            @RequestParam String id,
            @RequestHeader(value = ApiConstants.AUTHORIZATION_HEADER, required = false) String token) {
        logger.info("Received cart item count request for productId: {}", id);
        
        try {
            // 从JWT token中获取用户ID
            Integer userId = null;
            if (token != null && token.startsWith(ApiConstants.BEARER_PREFIX)) {
                try {
                    String jwtToken = token.substring(7);
                    userId = jwtUtil.getUserIdFromToken(jwtToken);
                    logger.debug("Extracted userId: {} from token", userId);
                } catch (Exception e) {
                    logger.warn("Failed to extract userId from token: {}", e.getMessage());
                }
            }
            
            // 如果没有有效的用户ID，使用默认值1（保持向后兼容）
            if (userId == null) {
                userId = 1;
                logger.debug("Using default userId: {}", userId);
            }
            
            // 从数据库获取购物车商品数量
            Map<String, Integer> data = cartService.getCartItemCount(userId, id);
            
            ResponseDTO<Map<String, Integer>> response = ResponseDTO.success(data);
            
            logger.info("Cart item count retrieved successfully for userId: {}, productId: {}, count: {}", 
                       userId, id, data.get("count"));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving cart item count: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Integer>> errorResponse = ResponseDTO.error(ApiConstants.CART_ITEM_COUNT_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 添加商品到购物车（新增或更新数量）
     * @param token JWT token (从Header中获取)
     * @return 添加结果
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> addToCart(
            @RequestBody Map<String, Object> requestBody,
            @RequestHeader(value = ApiConstants.AUTHORIZATION_HEADER, required = false) String token) {
        
        String productId = (String) requestBody.get("productId");
        Integer count = (Integer) requestBody.get("count");
        
        logger.info("Received add to cart request - productId: {}, count: {}", productId, count);
        
        try {
            // 从JWT token中获取用户ID
            Integer userId = null;
            if (token != null && token.startsWith(ApiConstants.BEARER_PREFIX)) {
                try {
                    String jwtToken = token.substring(7);
                    userId = jwtUtil.getUserIdFromToken(jwtToken);
                    logger.debug("Extracted userId: {} from token", userId);
                } catch (Exception e) {
                    logger.warn("Failed to extract userId from token: {}", e.getMessage());
                }
            }
            
            // 如果没有有效的用户ID，使用默认值1（保持向后兼容）
            if (userId == null) {
                userId = 1;
                logger.debug("Using default userId: {}", userId);
            }
            
            // 调用CartService添加到购物车
            Map<String, Object> data = cartService.addToCart(userId, productId, count);
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success(ApiConstants.CART_ADD_SUCCESS_MESSAGE, data);
            
            logger.info("Product {} {} successfully - userId: {}, productId: {}, count: {}", 
                       data.get("action"), userId, productId, count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error adding product to cart: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(ApiConstants.CART_ADD_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 更新购物车中商品的数量
     * @param id 商品ID
     * @param count 新的数量
     * @param token JWT token (从Header中获取)
     * @return 更新结果
     */
    @PostMapping("/change")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> changeCartItem(
            @RequestParam String id,
            @RequestParam int count,
            @RequestHeader(value = ApiConstants.AUTHORIZATION_HEADER, required = false) String token) {
        
        logger.info("Received cart change request for productId: {}, count: {}", id, count);
        
        try {
            // 从JWT token中获取用户ID
            Integer userId = null;
            if (token != null && token.startsWith(ApiConstants.BEARER_PREFIX)) {
                try {
                    String jwtToken = token.substring(7);
                    userId = jwtUtil.getUserIdFromToken(jwtToken);
                    logger.debug("Extracted userId: {} from token", userId);
                } catch (Exception e) {
                    logger.warn("Failed to extract userId from token: {}", e.getMessage());
                }
            }
            
            // 如果没有有效的用户ID，使用默认值1（保持向后兼容）
            if (userId == null) {
                userId = 1;
                logger.debug("Using default userId: {}", userId);
            }
            
            // 调用CartService更新购物车商品
            Map<String, Object> data = cartService.changeCartItem(userId, id, count);
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success(ApiConstants.CART_CHANGE_SUCCESS_MESSAGE, data);
            
            logger.info("Cart item {} successfully - userId: {}, productId: {}, count: {}", 
                       data.get("action"), userId, id, count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error updating cart item: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(ApiConstants.CART_CHANGE_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}