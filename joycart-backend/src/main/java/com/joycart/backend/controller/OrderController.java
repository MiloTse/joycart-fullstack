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
}
