package com.joycart.backend.service;

import com.joycart.backend.dto.CartItem;
import com.joycart.backend.model.Order;
import com.joycart.backend.model.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderService {
    
    /**
     * 提交订单
     * @param cartItems 购物车商品列表
     * @param userId 用户ID
     * @return 订单ID
     */
    String submitOrder(List<CartItem> cartItems, Integer userId);
    
    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 订单详情数据
     */
    Map<String, Object> getOrderDetail(String orderId, Integer userId);
    
    /**
     * 处理支付
     * @param orderId 订单ID
     * @param addressId 地址ID
     * @param deliveryTime 配送时间
     * @param paymentMethod 支付方式
     * @param userId 用户ID
     * @return 支付结果
     */
    Map<String, Object> processPayment(String orderId, String addressId, String deliveryTime, 
                                      String paymentMethod, Integer userId);
    
    /**
     * 获取用户订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Map<String, Object>> getUserOrders(Integer userId);
}
