package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 提交购物车，创建订单
     * @param cartItems 用户选中的商品列表
     * @return 订单ID
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitCart(@RequestBody List<CartItem> cartItems) {
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
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            Map<String, String> data = new HashMap<>();
            data.put("orderId", orderId);
            response.put("data", data);
            
            logger.info("Cart submitted successfully, generated orderId: {}", orderId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error submitting cart: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("data", null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * 获取用户地址列表
     * @return 用户地址列表
     */
    @GetMapping("/addresses")
    public ResponseEntity<?> getUserAddresses() {
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
            
            logger.info("User addresses retrieved successfully, found {} addresses", addressList.length);
            return ResponseEntity.ok(addressList);
            
        } catch (Exception e) {
            logger.error("Error retrieving user addresses: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new Object[]{});
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
