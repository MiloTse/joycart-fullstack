package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.CartItem;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.OrderService;
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
    private UserAddressService userAddressService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    
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
                return ResponseEntity.ok(ResponseDTO.error(ApiConstants.UNAUTHORIZED_MESSAGE));
            }
            
            // 使用OrderService提交订单
            String orderId = orderService.submitOrder(cartItems, userId);
            
            Map<String, String> data = new HashMap<>();
            data.put("orderId", orderId);
            
            ResponseDTO<Map<String, String>> response = ResponseDTO.success(ApiConstants.ORDER_SUBMIT_SUCCESS_MESSAGE, data);
            
            logger.info("Cart submitted successfully, generated orderId: {}", orderId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error submitting cart: {}", e.getMessage(), e);
            ResponseDTO<Map<String, String>> errorResponse = ResponseDTO.error(ApiConstants.ORDER_SUBMIT_FAILED_MESSAGE);
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
            ResponseDTO<Object[]> errorResponse = ResponseDTO.error(ApiConstants.USER_ADDRESSES_FAILED_MESSAGE);
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
                return ResponseEntity.ok(ResponseDTO.error(ApiConstants.UNAUTHORIZED_MESSAGE));
            }
            
            // 使用OrderService获取订单详情
            Map<String, Object> orderData = orderService.getOrderDetail(id, userId);
            
            if (orderData == null) {
                logger.warn("Order not found or access denied for orderId: {}, userId: {}", id, userId);
                return ResponseEntity.ok(ResponseDTO.error(ApiConstants.ORDER_NOT_FOUND_OR_ACCESS_DENIED_MESSAGE));
            }
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success(ApiConstants.ORDER_DETAIL_SUCCESS_MESSAGE, orderData);
            
            logger.info("Order detail retrieved successfully for orderId: {}", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving order detail: {}", e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(ApiConstants.ORDER_DETAIL_FAILED_MESSAGE);
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
                return ResponseEntity.ok(ResponseDTO.error(ApiConstants.UNAUTHORIZED_MESSAGE));
            }
            
            // 使用OrderService处理支付
            Map<String, Object> paymentResult = orderService.processPayment(orderId, addressId, time, payWay, userId);
            
            if (paymentResult == null) {
                logger.warn("Payment processing failed for order: {}", orderId);
                return ResponseEntity.ok(ResponseDTO.error(ApiConstants.ORDER_PAYMENT_FAILED_MESSAGE));
            }
            
            boolean paymentSuccess = (Boolean) paymentResult.get("success");
            ResponseDTO<Boolean> response = ResponseDTO.success(ApiConstants.ORDER_PAYMENT_SUCCESS_MESSAGE, paymentSuccess);
            
            if (paymentSuccess) {
                logger.info("Payment processed successfully for order: {}", orderId);
            } else {
                logger.warn("Payment failed for order: {}", orderId);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            ResponseDTO<Boolean> errorResponse = ResponseDTO.error(ApiConstants.ORDER_PAYMENT_PROCESS_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


}
