package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    /**
     * 获取订单详情
     * @param id 订单ID
     * @return 订单详情数据
     */
    @GetMapping("/detail")
    public ResponseEntity<?> getOrderDetail(@RequestParam String id) {
        logger.info("Received order detail request for orderId: {}", id);
        
        try {
            // 硬编码订单详情数据（模拟原始JSON）
            Map<String, Object> orderData = createOrderDetailData();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", orderData);
            
            logger.info("Order detail retrieved successfully for orderId: {}", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving order detail: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("data", null);
            return ResponseEntity.internalServerError().body(errorResponse);
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
    public ResponseEntity<?> processPayment(
            @RequestParam String orderId,
            @RequestParam String addressId,
            @RequestParam String time,
            @RequestParam String payWay) {
        
        logger.info("Received payment request - orderId: {}, addressId: {}, time: {}, payWay: {}", 
                   orderId, addressId, time, payWay);
        
        try {
            // 模拟支付处理逻辑
            boolean paymentSuccess = true; //暂时硬编码为成功，后续改为调用支付服务
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", paymentSuccess);
            response.put("data", paymentSuccess);
            
            if (paymentSuccess) {
                logger.info("Payment processed successfully for order: {}", orderId);
            } else {
                logger.warn("Payment failed for order: {}", orderId);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error processing payment: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("data", false);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 创建订单详情数据（硬编码模拟数据）
     */
    private Map<String, Object> createOrderDetailData() {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("balance", 200);
        
        // 时间选择范围
        orderData.put("timeRange", createTimeRange());
        
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

    private Object createTimeRange() {
        // 简化时间范围数据
        return new Object[][][]{
            {{"label", "2025-05-09"}, {"value", "2025-05-09"}},
            {{"label", "09"}, {"value", "09"}, {"label", "10"}, {"value", "10"}},
            {{"label", "00"}, {"value", "00"}, {"label", "15"}, {"value", "15"}}
        };
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
}
