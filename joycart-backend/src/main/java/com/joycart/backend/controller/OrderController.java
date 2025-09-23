package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.joycart.backend.dto.ResponseDTO;

import java.util.*;

/**
 * 购物车商品项数据结构
 */
class CartItem {
    private String productId;
    private Integer count;
    
    // 构造函数
    public CartItem() {}
    
    public CartItem(String productId, Integer count) {
        this.productId = productId;
        this.count = count;
    }
    
    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    
    @Override
    public String toString() {
        return "CartItem{productId='" + productId + "', count=" + count + "}";
    }
}

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    //先模拟订单数据
    // Key: orderId, Value: selected productList
    private static final Map<String, List<CartItem>> orderStorage = new HashMap<>();
    
    // 商品信息存储 (实际项目中应该从数据库查询)
    // Key: productId, Value: 商品详细信息
    private static final Map<String, Map<String, Object>> productInfoStorage = new HashMap<>();
    
    static {
        // 初始化商品信息数据，与Cart页面的硬编码数据保持一致
        Map<String, Object> product88391 = new HashMap<>();
        product88391.put("productId", "88391");
        product88391.put("title", "Sweet Radish 10 lbs - Crisp and Sweet, Perfect for Salads");
        product88391.put("price", 14.9);
        product88391.put("imgUrl", "/images/external/category-list-5.png");
        productInfoStorage.put("88391", product88391);
        
        Map<String, Object> product88392 = new HashMap<>();
        product88392.put("productId", "88392");
        product88392.put("title", "Australian Beef Rolls 450g - Ideal for Hot Pot and BBQ");
        product88392.put("price", 35.0);
        product88392.put("imgUrl", "/images/external/category-list-3.png");
        productInfoStorage.put("88392", product88392);
        
        Map<String, Object> product89391 = new HashMap<>();
        product89391.put("productId", "89391");
        product89391.put("title", "Fresh Snapper 900g - Cleaned and Ready to Cook");
        product89391.put("price", 69.9);
        product89391.put("imgUrl", "/images/external/category-list-6.png");
        productInfoStorage.put("89391", product89391);
    }
    
    /**
     * 提交购物车，创建订单
     * @param cartItems 用户选中的商品列表
     * @return 订单ID
     */
    @PostMapping("/submit")
    public ResponseEntity<ResponseDTO<Map<String, String>>> submitCart(@RequestBody List<CartItem> cartItems) {
        logger.info("Received cart submit request with {} items", cartItems != null ? cartItems.size() : 0);
        
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                logger.info("Selected item: {}", item);
            }
        }
        
        try {
            //生成唯一订单ID（简单实现）
            String orderId = "ORDER_" + System.currentTimeMillis();
            
            // 保存选中的商品到订单存储, 后续改为使用db
            if (cartItems != null && !cartItems.isEmpty()) {
                orderStorage.put(orderId, new ArrayList<>(cartItems));
                logger.info("Stored {} items for order: {}", cartItems.size(), orderId);
            } else {
                logger.warn("No items provided for order: {}", orderId);
            }
            
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
     * @return 用户地址列表
     */
    @GetMapping("/addresses")
    public ResponseEntity<ResponseDTO<Object[]>> getUserAddresses() {
        logger.info("Received user addresses request");
        
        try {
            // 硬编码用户地址数据（实际项目中应该根据JWT token获取用户的地址）
            Object[] addressList = {
                createAddressItem("1", "John Zhang", "13800138000", 
                    "Room 101, Unit 1, Building 1, Residential Complex, Chaoyang District, Beijing", true),
                createAddressItem("2", "Mike Li", "13900139000", 
                    "Room 2001, 20th Floor, Office Building, Pudong New Area, Shanghai", false),
                createAddressItem("3", "Jerry Wang", "1-613-727-4723", 
                    "1385 Woodroffe Avenue, Ottawa, ON, K2G 1V8", false)
            };
            
            ResponseDTO<Object[]> response = ResponseDTO.success("用户地址列表获取成功", addressList);
            
            logger.info("User addresses retrieved successfully, found {} addresses", addressList.length);
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
    public ResponseEntity<?> getOrderDetail(@RequestParam String id) {
        logger.info("Received order detail request for orderId: {}", id);
        
        try {
            // 第1步：从订单存储中获取选中的商品列表
            List<CartItem> orderItems = orderStorage.get(id);
            
            Map<String, Object> orderData;
            if (orderItems != null && !orderItems.isEmpty()) {
                // 根据实际订单商品生成动态数据
                orderData = createDynamicOrderDetailData(id, orderItems);
                logger.info("Generated dynamic order detail for {} items", orderItems.size());
                
                for (CartItem item : orderItems) {
                    logger.info("Order item: productId={}, count={}", item.getProductId(), item.getCount());
                }
            } else {
                // 如果没有找到订单数据，使用默认数据（兼容旧订单）
                logger.warn("No order items found for orderId: {}, using default data", id);
                orderData = createOrderDetailData();
            }
            
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
     * 根据实际订单商品创建动态订单详情数据
     */
    private Map<String, Object> createDynamicOrderDetailData(String orderId, List<CartItem> orderItems) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("balance", 200);
        
        // 时间选择范围（保持不变）
        orderData.put("timeRange", createTimeRange());
        
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
            
            // 从商品信息存储中获取商品详情
            Map<String, Object> productInfo = productInfoStorage.get(productId);
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
        
        // 设置动态计算的总价
        orderData.put("total", (int) Math.round(totalPrice));
        
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

    private List<List<Map<String, String>>> createTimeRange() {
        // 创建时间选择器数据格式（参考原始orderDetail.json格式）
        List<List<Map<String, String>>> timeRange = new ArrayList<>();
        
        // 日期选项（第一列）
        List<Map<String, String>> dateOptions = Arrays.asList(
            Map.of("label", "2025-05-09", "value", "2025-05-09"),
            Map.of("label", "2025-05-10", "value", "2025-05-10"),
            Map.of("label", "2025-05-11", "value", "2025-05-11")
        );
        
        // 小时选项（第二列）
        List<Map<String, String>> hourOptions = Arrays.asList(
            Map.of("label", "09", "value", "09"),
            Map.of("label", "10", "value", "10"),
            Map.of("label", "11", "value", "11")
        );
        
        // 分钟选项（第三列）
        List<Map<String, String>> minuteOptions = Arrays.asList(
            Map.of("label", "00", "value", "00"),
            Map.of("label", "15", "value", "15"),
            Map.of("label", "30", "value", "30"),
            Map.of("label", "45", "value", "45")
        );
        
        timeRange.add(dateOptions);
        timeRange.add(hourOptions);
        timeRange.add(minuteOptions);
        
        return timeRange;
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
