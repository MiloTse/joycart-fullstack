package com.joycart.backend.service.impl;

import com.joycart.backend.model.Cart;
import com.joycart.backend.repository.CartRepository;
import com.joycart.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public Map<String, Object> addToCart(Integer userId, String productId, Integer count) {
        logger.debug("Adding to cart - userId: {}, productId: {}, count: {}", userId, productId, count);
        
        try {
            // 查找是否已存在该商品
            Optional<Cart> existingCartOptional = cartRepository.findByUserIdAndProductIdAndIsActiveTrue(userId, productId);
            
            String action;
            Cart cartItem;
            
            if (existingCartOptional.isPresent()) {
                // 更新现有商品数量
                cartItem = existingCartOptional.get();
                cartItem.setQuantity(count);
                cartItem.setUpdatedAt(LocalDateTime.now());
                action = "updated";
                logger.debug("Updated existing cart item for userId: {}, productId: {}, new count: {}", userId, productId, count);
            } else {
                // 创建新的购物车商品
                cartItem = new Cart(userId, productId, count);
                action = "added";
                logger.debug("Created new cart item for userId: {}, productId: {}, count: {}", userId, productId, count);
            }
            
            // 保存到数据库
            cartRepository.save(cartItem);
            
            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("productId", productId);
            result.put("count", count);
            result.put("action", action);
            result.put("timestamp", LocalDateTime.now().toString());
            
            logger.info("Successfully {} product to cart - userId: {}, productId: {}, count: {}", action, userId, productId, count);
            return result;
            
        } catch (Exception e) {
            logger.error("Error adding product to cart for userId: {}, productId: {}, count: {} - {}", userId, productId, count, e.getMessage(), e);
            // 发生异常时返回错误信息
            Map<String, Object> result = new HashMap<>();
            result.put("productId", productId);
            result.put("count", 0);
            result.put("action", "error");
            result.put("timestamp", LocalDateTime.now().toString());
            return result;
        }
    }

    @Override
    public Map<String, Object> changeCartItem(Integer userId, String productId, Integer count) {
        logger.debug("Changing cart item - userId: {}, productId: {}, count: {}", userId, productId, count);
        
        try {
            // 查找是否已存在该商品
            Optional<Cart> existingCartOptional = cartRepository.findByUserIdAndProductIdAndIsActiveTrue(userId, productId);
            
            String action;
            Map<String, Object> result = new HashMap<>();
            
            if (count <= 0) {
                // 数量为0或负数时，从购物车中移除商品
                if (existingCartOptional.isPresent()) {
                    Cart cartItem = existingCartOptional.get();
                    cartItem.setIsActive(false);
                    cartItem.setUpdatedAt(LocalDateTime.now());
                    cartRepository.save(cartItem);
                    action = "removed";
                    logger.debug("Removed cart item for userId: {}, productId: {}", userId, productId);
                } else {
                    action = "not_found";
                    logger.debug("Cart item not found for removal - userId: {}, productId: {}", userId, productId);
                }
                result.put("count", 0);
            } else {
                // 更新商品数量
                if (existingCartOptional.isPresent()) {
                    Cart cartItem = existingCartOptional.get();
                    cartItem.setQuantity(count);
                    cartItem.setUpdatedAt(LocalDateTime.now());
                    cartRepository.save(cartItem);
                    action = "updated";
                    logger.debug("Updated cart item for userId: {}, productId: {}, new count: {}", userId, productId, count);
                } else {
                    // 如果商品不存在，创建新的购物车商品
                    Cart newCartItem = new Cart(userId, productId, count);
                    cartRepository.save(newCartItem);
                    action = "added";
                    logger.debug("Added new cart item for userId: {}, productId: {}, count: {}", userId, productId, count);
                }
                result.put("count", count);
            }
            
            // 构建返回数据
            result.put("id", Integer.parseInt(productId));
            result.put("action", action);
            result.put("updatedAt", LocalDateTime.now().toString());
            
            logger.info("Successfully {} cart item - userId: {}, productId: {}, count: {}", action, userId, productId, count);
            return result;
            
        } catch (Exception e) {
            logger.error("Error changing cart item for userId: {}, productId: {}, count: {} - {}", userId, productId, count, e.getMessage(), e);
            // 发生异常时返回错误信息
            Map<String, Object> result = new HashMap<>();
            result.put("id", Integer.parseInt(productId));
            result.put("count", 0);
            result.put("action", "error");
            result.put("updatedAt", LocalDateTime.now().toString());
            return result;
        }
    }

    @Override
    public Object[] getCartProducts(Integer userId) {
        logger.debug("Getting cart products for userId: {}", userId);
        
        try {
            // 查找用户的所有活跃购物车商品
            List<Cart> cartItems = cartRepository.findByUserIdAndIsActiveTrue(userId);
            
            if (cartItems.isEmpty()) {
                logger.info("No cart items found for userId: {}", userId);
                return new Object[0];
            }
            
            // 按商店分组（这里简化处理，所有商品归为一个商店）
            Map<String, Object> shopData = createShopCartData("8137", "Mei's Fresh Produce", cartItems);
            
            Object[] result = {shopData};
            
            logger.info("Successfully retrieved {} cart items for userId: {}", cartItems.size(), userId);
            return result;
            
        } catch (Exception e) {
            logger.error("Error getting cart products for userId: {} - {}", userId, e.getMessage(), e);
            // 发生异常时返回空数组
            return new Object[0];
        }
    }
    
    private Map<String, Object> createShopCartData(String shopId, String shopName, List<Cart> cartItems) {
        Map<String, Object> shop = new HashMap<>();
        shop.put("shopId", shopId);
        shop.put("shopName", shopName);
        
        // 转换Cart实体为前端期望的格式
        List<Map<String, Object>> cartList = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            Map<String, Object> product = new HashMap<>();
            product.put("productId", cartItem.getProductId());
            product.put("imgUrl", "/images/external/category-list-5.png"); // 默认图片
            product.put("title", "Product " + cartItem.getProductId()); // 默认标题
            product.put("price", 14.9); // 默认价格
            product.put("count", cartItem.getQuantity());
            cartList.add(product);
        }
        
        shop.put("cartList", cartList.toArray());
        return shop;
    }
}
