package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.joycart.backend.dto.ResponseDTO;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getProductDetail(@PathVariable String id) {
        logger.info("Received product detail request for id: {}", id);
        
        try {
            // 硬编码商品详情数据（模拟原始JSON）
            Map<String, Object> productDetail = createMockProductDetail(id);
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success("商品详情获取成功", productDetail);
            
            logger.info("Product detail retrieved successfully for id: {}", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving product detail for id: {} - {}", id, e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error("获取商品详情失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    private Map<String, Object> createMockProductDetail(String id) {
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("imgUrl", "/images/external/detail.png");
        product.put("title", "Shandong Haiyang Provence Tomatoes - Natural Ripe Sandy Tomatoes Fresh Fruit Vegetables Healthy Food Premium Box 5kg");
        product.put("subtitle", "Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce");
        product.put("price", 39.9);
        product.put("sales", 456);
        product.put("origin", "Based on actual purchased product batch");
        product.put("specification", "2kg");
        product.put("detail", "Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.");
        return product;
    }
}
