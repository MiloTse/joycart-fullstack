package com.joycart.backend.service.impl;

import com.joycart.backend.dto.CartItem;
import com.joycart.backend.model.Order;
import com.joycart.backend.model.OrderItem;
import com.joycart.backend.model.Product;
import com.joycart.backend.repository.OrderItemRepository;
import com.joycart.backend.repository.OrderRepository;
import com.joycart.backend.service.DeliveryTimeService;
import com.joycart.backend.service.OrderService;
import com.joycart.backend.service.ProductService;
import com.joycart.backend.service.UserAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserAddressService userAddressService;
    
    @Autowired
    private DeliveryTimeService deliveryTimeService;

    @Override
    @Transactional
    public String submitOrder(List<CartItem> cartItems, Integer userId) {
        logger.debug("Submitting order for user: {}", userId);
        
        // 生成订单ID
        String orderId = "ORDER" + System.currentTimeMillis();
        
        // 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            Map<String, Object> productData = productService.getProductDetail(item.getProductId());
            if (productData != null && productData.get("price") != null) {
                Double price = (Double) productData.get("price");
                BigDecimal itemTotal = BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(item.getCount()));
                totalAmount = totalAmount.add(itemTotal);
            }
        }
        
        // 创建订单
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);
        logger.info("Order created with ID: {}", orderId);
        
        // 创建订单项
        for (CartItem item : cartItems) {
            Map<String, Object> productData = productService.getProductDetail(item.getProductId());
            if (productData != null && productData.get("price") != null) {
                Double price = (Double) productData.get("price");
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getCount());
                orderItem.setPrice(BigDecimal.valueOf(price));
                orderItem.setCreatedAt(LocalDateTime.now());
                orderItem.setUpdatedAt(LocalDateTime.now());
                
                orderItemRepository.save(orderItem);
            }
        }
        
        logger.info("Order items created for order: {}", orderId);
        return orderId;
    }

    @Override
    public Map<String, Object> getOrderDetail(String orderId, Integer userId) {
        logger.debug("Getting order detail for orderId: {}, userId: {}", orderId, userId);
        
        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (!orderOpt.isPresent()) {
            logger.warn("Order not found: {}", orderId);
            return null;
        }
        
        Order order = orderOpt.get();
        if (!order.getUserId().equals(userId)) {
            logger.warn("Order {} does not belong to user {}", orderId, userId);
            return null;
        }
        
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("balance", 200);
        
        // 时间选择范围（动态生成）
        orderData.put("timeRange", deliveryTimeService.getAvailableDeliveryTimes());
        
        // 获取用户地址列表
        List<Map<String, Object>> addresses = userAddressService.getUserAddresses(userId);
        if (addresses != null && !addresses.isEmpty()) {
            orderData.put("address", addresses.get(0)); // 使用第一个地址作为默认地址
        } else {
            // 如果没有地址，使用默认地址
            Map<String, String> defaultAddress = new HashMap<>();
            defaultAddress.put("id", "10036");
            defaultAddress.put("name", "Jerry Wang");
            defaultAddress.put("phone", "1-613-727-4723");
            defaultAddress.put("address", "1385 Woodroffe Avenue, Ottawa, ON, K2G 1V8");
            orderData.put("address", defaultAddress);
        }
        
        // 默认时间
        orderData.put("time", new String[]{"2025-05-09", "09", "00"});
        orderData.put("total", order.getTotalAmount().doubleValue());
        
        // 获取订单商品信息
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        orderData.put("shop", createShopDataFromOrderItems(orderItems));
        
        logger.info("Order detail retrieved for order: {}", orderId);
        return orderData;
    }

    @Override
    @Transactional
    public Map<String, Object> processPayment(String orderId, String addressId, String deliveryTime, 
                                            String paymentMethod, Integer userId) {
        logger.debug("Processing payment for order: {}, userId: {}", orderId, userId);
        
        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (!orderOpt.isPresent()) {
            logger.warn("Order not found: {}", orderId);
            return null;
        }
        
        Order order = orderOpt.get();
        if (!order.getUserId().equals(userId)) {
            logger.warn("Order {} does not belong to user {}", orderId, userId);
            return null;
        }
        
        // 解析配送时间
        String[] timeParts = deliveryTime.split(" ");
        if (timeParts.length >= 2) {
            String dateStr = timeParts[0];
            String timeStr = timeParts[1];
            
            try {
                LocalDate deliveryDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                order.setDeliveryDate(deliveryDate);
                order.setDeliveryTime(timeStr);
            } catch (Exception e) {
                logger.error("Error parsing delivery time: {}", deliveryTime, e);
            }
        }
        
        // 更新订单信息
        order.setAddressId(Long.valueOf(addressId));
        order.setPaymentMethod(paymentMethod);
        order.setStatus("PAID");
        order.setUpdatedAt(LocalDateTime.now());
        
        orderRepository.save(order);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "支付成功");
        result.put("orderId", orderId);
        result.put("paymentMethod", paymentMethod);
        result.put("deliveryTime", deliveryTime);
        
        logger.info("Payment processed successfully for order: {}", orderId);
        return result;
    }

    @Override
    public List<Map<String, Object>> getUserOrders(Integer userId) {
        logger.debug("Getting orders for user: {}", userId);
        
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<Map<String, Object>> orderList = new ArrayList<>();
        
        for (Order order : orders) {
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("orderId", order.getOrderId());
            orderInfo.put("totalAmount", order.getTotalAmount());
            orderInfo.put("status", order.getStatus());
            orderInfo.put("createdAt", order.getCreatedAt());
            orderInfo.put("deliveryDate", order.getDeliveryDate());
            orderInfo.put("deliveryTime", order.getDeliveryTime());
            orderList.add(orderInfo);
        }
        
        logger.info("Retrieved {} orders for user: {}", orderList.size(), userId);
        return orderList;
    }
    
    /**
     * 根据订单项创建商店数据
     */
    private Object createShopDataFromOrderItems(List<OrderItem> orderItems) {
        Map<String, Object> shop = new HashMap<>();
        shop.put("shopId", "8137");
        shop.put("shopName", "Mei's Fresh Produce");
        
        List<Map<String, Object>> cartList = new ArrayList<>();
        
        for (OrderItem item : orderItems) {
            Map<String, Object> productData = productService.getProductDetail(item.getProductId());
            if (productData != null) {
                Map<String, Object> productInfo = new HashMap<>();
                productInfo.put("productId", item.getProductId());
                productInfo.put("imgUrl", productData.get("imgUrl"));
                productInfo.put("weight", "0.45kg");
                productInfo.put("title", productData.get("title"));
                productInfo.put("price", productData.get("price"));
                productInfo.put("count", item.getQuantity());
                cartList.add(productInfo);
            }
        }
        
        shop.put("cartList", cartList.toArray());
        return new Object[]{shop};
    }
}
