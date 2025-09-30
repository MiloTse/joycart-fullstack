package com.joycart.backend.controller;

import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.CartService;
import com.joycart.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    // Key: productId, Value: count
    private static final Map<String, Integer> cartStorage = new HashMap<>();
    
    static {
        //测试数据，模拟用户已有的购物车商品
        cartStorage.put("88391", 2);
        cartStorage.put("88392", 1);
        cartStorage.put("89391", 1);
    }

    /**
     * 获取购物车商品列表
     * @return 购物车中所有商品列表
     */
    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<Object[]>> getCartProducts() {
        logger.info("Received cart products request");
        
        try {
            // 硬编码购物车商品数据（实际项目中应该根据用户ID从数据库查询）
            Object[] cartData = {
                createShopCartData("8137", "Mei's Fresh Produce", new Object[]{
                    createCartProduct("88391", "/images/external/category-list-5.png", 
                        "Sweet Radish 10 lbs - Crisp and Sweet, Perfect for Salads", 14.9, 2),
                    createCartProduct("88392", "/images/external/category-list-3.png", 
                        "Australian Beef Rolls 450g - Ideal for Hot Pot and BBQ", 35.0, 1)
                }),
                createShopCartData("8318", "Gourmet Delights", new Object[]{
                    createCartProduct("89391", "/images/external/category-list-6.png", 
                        "Fresh Snapper 900g - Cleaned and Ready to Cook", 69.9, 1)
                })
            };
            
            ResponseDTO<Object[]> response = ResponseDTO.success("购物车商品列表获取成功", cartData);
            
            logger.info("Cart products retrieved successfully, found {} shops", cartData.length);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving cart products: {}", e.getMessage(), e);
            ResponseDTO<Object[]> errorResponse = ResponseDTO.error("获取购物车商品列表失败，请重试");
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
            // 硬编码商品购物车信息（实际项目中应该从数据库根据商品ID查询）
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("id", productId);
            productInfo.put("name", "Shandong Haian Provolone cherry tomatoes natural seeds potato fruit vegetables healthy snack");
            productInfo.put("price", 39.9);
            productInfo.put("imgUrl", "/images/banner01.png");
            productInfo.put("count", 0);
            productInfo.put("sales", 456);
            productInfo.put("specification", "5kg package");
            productInfo.put("origin", "Shandong Haian");
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success("商品购物车信息获取成功", productInfo);
            
            logger.info("Cart product info retrieved successfully for productId: {}", productId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving cart product info: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error("获取商品购物车信息失败，请重试");
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
            @RequestHeader(value = "Authorization", required = false) String token) {
        logger.info("Received cart item count request for productId: {}", id);
        
        try {
            // 从JWT token中获取用户ID
            Integer userId = null;
            if (token != null && token.startsWith("Bearer ")) {
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
            ResponseDTO<Map<String, Integer>> errorResponse = ResponseDTO.error("Failed to retrieve cart item count");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 添加商品到购物车（新增或更新数量）
     * @param productId 商品ID
     * @param count 商品数量
     * @param token JWT token (从Header中获取)
     * @return 添加结果
     */
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> addToCart(
            @RequestParam String productId,
            @RequestParam int count,
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        logger.info("Received add to cart request - productId: {}, count: {}", productId, count);
        
        try {
            // 从JWT token中获取用户ID
            Integer userId = null;
            if (token != null && token.startsWith("Bearer ")) {
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
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success("商品已成功添加到购物车", data);
            
            logger.info("Product {} {} successfully - userId: {}, productId: {}, count: {}", 
                       data.get("action"), userId, productId, count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error adding product to cart: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error("添加到购物车失败，请重试");
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
            @RequestHeader(value = "Authorization", required = false) String token) {
        
        logger.info("Received cart change request for productId: {}, count: {}", id, count);
        
        try {
            // 从JWT token中获取用户ID
            Integer userId = null;
            if (token != null && token.startsWith("Bearer ")) {
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
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success("购物车更新成功", data);
            
            logger.info("Cart item {} successfully - userId: {}, productId: {}, count: {}", 
                       data.get("action"), userId, id, count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error updating cart item: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error("购物车更新失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    private Map<String, Object> createShopCartData(String shopId, String shopName, Object[] cartList) {
        Map<String, Object> shop = new HashMap<>();
        shop.put("shopId", shopId);
        shop.put("shopName", shopName);
        shop.put("cartList", cartList);
        return shop;
    }

    private Map<String, Object> createCartProduct(String productId, String imgUrl, String title, 
                                                  double price, int count) {
        Map<String, Object> product = new HashMap<>();
        product.put("productId", productId);
        product.put("imgUrl", imgUrl);
        product.put("title", title);
        product.put("price", price);
        product.put("count", count);
        return product;
    }
}