package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.CartItem;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.DeliveryTimeService;
import com.joycart.backend.service.OrderService;
import com.joycart.backend.service.ProductService;
import com.joycart.backend.service.UserAddressService;
import com.joycart.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserAddressService userAddressService;
    
    @Autowired
    private DeliveryTimeService deliveryTimeService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // Key: orderId, Value: selected productList
    private static final Map<String, List<CartItem>> orderStorage = new HashMap<>();
    
    
    /**
     * 提交购物车，创建订单
     * @param cartItems 用户选中的商品列表
     * @return 订单ID
     */
    @PostMapping("/submit")
    public ResponseEntity<ResponseDTO<Map<String, String>>> submitCart(@RequestBody List<CartItem> cartItems,
                                                                       @RequestHeader(ApiConstants.AUTHORIZATION_HEADER) String authorization) {
        logger.info("Received cart submit request with {} items", cartItems != null ? cartItems.size() : 0);
        
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                logger.info("Selected item: {}", item);
            }
        }
        
        try {
            // 从Authorization头中提取JWT token
            String token = authorization.replace(ApiConstants.BEARER_PREFIX, "");
            Integer userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                logger.warn("Unable to extract user ID from token");
                return ResponseEntity.ok(ResponseDTO.error("用户认证失败"));
            }
            
            // 使用OrderService提交订单
            String orderId = orderService.submitOrder(cartItems, userId);
            
            Map<String, String> data = new HashMap<>();
            data.put("orderId", orderId);
            
            ResponseDTO<Map<String, String>> response = ResponseDTO.success("订单提交成功", data);
            
            logger.info("Cart submitted successfully, generated orderId: {}", orderId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error submitting cart: {}", e.getMessage(), e);
            ResponseDTO<Map<String, String>> errorResponse = ResponseDTO.error("订单提交失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 获取用户地址列表
     * @param authorization JWT token
     * @return 用户地址列表
     */
    @GetMapping("/addresses")
    public ResponseEntity<ResponseDTO<Object[]>> getUserAddresses(
            @RequestHeader(ApiConstants.AUTHORIZATION_HEADER) String authorization) {
        logger.info("Received user addresses request");
        
        try {
            // 从JWT token中提取用户ID
            String token = authorization.replace(ApiConstants.BEARER_PREFIX, "");
            Integer userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                logger.warn("Invalid token or user not found");
                return ResponseEntity.badRequest().body(ResponseDTO.error(ApiConstants.UNAUTHORIZED_MESSAGE));
            }
            
            // 从数据库获取用户地址数据
            List<Map<String, Object>> addressList = userAddressService.getUserAddresses(userId);
            
            if (addressList == null) {
                logger.warn("No addresses found for userId: {}", userId);
                return ResponseEntity.badRequest().body(ResponseDTO.error(ApiConstants.USER_ADDRESSES_NOT_FOUND_MESSAGE));
            }
            
            // 转换为Object[]格式以保持前端兼容性
            Object[] addressArray = addressList.toArray();
            
            ResponseDTO<Object[]> response = ResponseDTO.success(ApiConstants.USER_ADDRESSES_SUCCESS_MESSAGE, addressArray);
            
            logger.info("User addresses retrieved successfully from database, found {} addresses for userId: {}", 
                       addressArray.length, userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving user addresses: {}", e.getMessage(), e);
            ResponseDTO<Object[]> errorResponse = ResponseDTO.error("获取用户地址失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 获取订单详情
     * @param id 订单ID
     * @return 订单详情数据
     */
    @GetMapping("/detail")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getOrderDetail(@RequestParam String id,
                                                                           @RequestHeader(ApiConstants.AUTHORIZATION_HEADER) String authorization) {
        logger.info("Received order detail request for orderId: {}", id);
        
        try {
            // 从Authorization头中提取JWT token
            String token = authorization.replace(ApiConstants.BEARER_PREFIX, "");
            Integer userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                logger.warn("Unable to extract user ID from token");
                return ResponseEntity.ok(ResponseDTO.error("用户认证失败"));
            }
            
            // 使用OrderService获取订单详情
            Map<String, Object> orderData = orderService.getOrderDetail(id, userId);
            
            if (orderData == null) {
                logger.warn("Order not found or access denied for orderId: {}, userId: {}", id, userId);
                return ResponseEntity.ok(ResponseDTO.error("订单不存在或无权限访问"));
            }
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success("订单详情获取成功", orderData);
            
            logger.info("Order detail retrieved successfully for orderId: {}", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving order detail: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error("获取订单详情失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 处理支付请求
     * @param orderId 订单ID
     * @param addressId 地址ID
     * @param time 配送时间
     * @param payWay 支付方式
     * @return 支付结果
     */
    @PostMapping("/pay")
    public ResponseEntity<ResponseDTO<Boolean>> processPayment(
            @RequestParam String orderId,
            @RequestParam String addressId,
            @RequestParam String time,
            @RequestParam String payWay,
            @RequestHeader(ApiConstants.AUTHORIZATION_HEADER) String authorization) {
        
        logger.info("Received payment request - orderId: {}, addressId: {}, time: {}, payWay: {}", 
                   orderId, addressId, time, payWay);
        
        try {
            // 从Authorization头中提取JWT token
            String token = authorization.replace(ApiConstants.BEARER_PREFIX, "");
            Integer userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                logger.warn("Unable to extract user ID from token");
                return ResponseEntity.ok(ResponseDTO.error("用户认证失败"));
            }
            
            // 使用OrderService处理支付
            Map<String, Object> paymentResult = orderService.processPayment(orderId, addressId, time, payWay, userId);
            
            if (paymentResult == null) {
                logger.warn("Payment processing failed for order: {}", orderId);
                return ResponseEntity.ok(ResponseDTO.error("支付处理失败"));
            }
            
            boolean paymentSuccess = (Boolean) paymentResult.get("success");
            ResponseDTO<Boolean> response = ResponseDTO.success("支付处理成功", paymentSuccess);
            
            if (paymentSuccess) {
                logger.info("Payment processed successfully for order: {}", orderId);
            } else {
                logger.warn("Payment failed for order: {}", orderId);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            ResponseDTO<Boolean> errorResponse = ResponseDTO.error("支付处理失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 根据实际订单商品创建动态订单详情数据
     */
    private Map<String, Object> createDynamicOrderDetailData(String orderId, List<CartItem> orderItems) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("balance", 200);
        
        // 时间选择范围（动态生成）
        orderData.put("timeRange", deliveryTimeService.getAvailableDeliveryTimes());
        
        // 默认地址（保持不变）
        Map<String, String> address = new HashMap<>();
        address.put("id", "10036");
        address.put("name", "Jerry Wang");
        address.put("phone", "1-613-727-4723");
        address.put("address", "1385 Woodroffe Avenue, Ottawa, ON, K2G 1V8");
        orderData.put("address", address);
        
        // 默认时间（保持不变）
        orderData.put("time", new String[]{"2025-05-09", "09", "00"});
        
        // 根据实际订单商品动态生成商店和商品信息
        List<Map<String, Object>> shopList = new ArrayList<>();
        double totalPrice = 0.0;
        
        // 简化处理：假设所有商品都来自同一个商店
        Map<String, Object> shop = new HashMap<>();
        shop.put("shopId", "8137");
        shop.put("shopName", "Mei's Fresh Produce");
        
        List<Map<String, Object>> productList = new ArrayList<>();
        
        for (CartItem item : orderItems) {
            String productId = item.getProductId();
            Integer count = item.getCount();
            
            // 从ProductService获取商品详情
            Map<String, Object> productInfo = productService.getProductDetail(productId);
            if (productInfo != null) {
                Map<String, Object> product = new HashMap<>();
                product.put("productId", productId);
                product.put("imgUrl", productInfo.get("imgUrl"));
                product.put("title", productInfo.get("title"));
                product.put("price", productInfo.get("price"));
                product.put("count", count);
                
                productList.add(product);
                
                // 计算总价
                Double price = (Double) productInfo.get("price");
                totalPrice += price * count;
                
                logger.info("Added product to order: {} x {} = ${}", 
                    productInfo.get("title"), count, price * count);
            } else {
                logger.warn("Product info not found for productId: {}", productId);
            }
        }
        
        shop.put("cartList", productList);
        shopList.add(shop);
        orderData.put("shop", shopList);
        
        // 设置动态计算的总价（保留两位小数精度）
        orderData.put("total", Math.round(totalPrice * 100.0) / 100.0);
        
        logger.info("Dynamic order total: ${}", totalPrice);
        logger.info("Order data structure: shop array size = {}", shopList.size());
        logger.info("Full order data keys: {}", orderData.keySet());
        return orderData;
    }

    /**
     * 创建订单详情数据（硬编码模拟数据）
     */
    private Map<String, Object> createOrderDetailData() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("balance", 200);
        
        // 时间选择范围（动态生成）
        orderData.put("timeRange", deliveryTimeService.getAvailableDeliveryTimes());
        
        // 默认地址
        Map<String, String> address = new HashMap<>();
        address.put("id", "10036");
        address.put("name", "Jerry Wang");
        address.put("phone", "1-613-727-4723");
        address.put("address", "1385 Woodroffe Avenue, Ottawa, ON, K2G 1V8");
        orderData.put("address", address);
        
        // 默认时间
        orderData.put("time", new String[]{"2025-05-09", "09", "00"});
        orderData.put("total", 630);
        
        // 商店和商品信息
        orderData.put("shop", createShopData());
        
        return orderData;
    }


    private Object createShopData() {
        // 简化商店数据
        Map<String, Object> shop1 = new HashMap<>();
        shop1.put("shopId", "8137");
        shop1.put("shopName", "Mei's Fresh Produce");
        
        Map<String, Object> product1 = new HashMap<>();
        product1.put("productId", "88391");
        product1.put("imgUrl", "/images/external/category-list-5.png");
        product1.put("weight", "0.45kg");
        product1.put("title", "Sweet Radish 10 lbs - Crisp and Sweet, Perfect for Salads");
        product1.put("price", 14.9);
        product1.put("count", 2);
        
        shop1.put("cartList", new Object[]{product1});
        
        return new Object[]{shop1};
    }


    private Map<String, Object> createAddressItem(String id, String name, String phone, 
                                                  String address, boolean isDefault) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("name", name);
        item.put("phone", phone);
        item.put("address", address);
        item.put("isDefault", isDefault);
        return item;
    }
}
