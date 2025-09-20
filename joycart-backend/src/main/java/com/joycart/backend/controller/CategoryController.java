package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @GetMapping("/list")
    public ResponseEntity<?> getCategoryAndTagList() {
        logger.info("Received category and tag list request");
        
        try {
            // 硬编码分类数据（模拟原始JSON）
            List<Map<String, String>> categories = Arrays.asList(
                createCategoryMap("8210", "精选商品"),
                createCategoryMap("8211", "单品优惠"),
                createCategoryMap("8212", "新鲜水果"),
                createCategoryMap("8213", "时令蔬菜"),
                createCategoryMap("8214", "肉蛋家禽"),
                createCategoryMap("8215", "水产海鲜"),
                createCategoryMap("8216", "牛奶面包"),
                createCategoryMap("8217", "冷冻冷藏"),
                createCategoryMap("8218", "米面粮油"),
                createCategoryMap("8219", "生活用品"),
                createCategoryMap("8220", "进口食品"),
                createCategoryMap("8221", "精美礼盒")
            );
            
            // 硬编码标签数据
            List<String> tags = Arrays.asList("果蔬", "肉蛋家禽", "海鲜");
            
            // 构造响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("category", categories);
            data.put("tag", tags);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", data);
            
            logger.info("Category and tag list retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving category and tag list: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("data", null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/products")
    public ResponseEntity<?> getCategoryProducts() {
        logger.info("Received category products request");
        
        try {
            // 硬编码商品列表数据（模拟原始JSON）
            List<Map<String, Object>> products = new ArrayList<>();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", products);
            
            logger.info("Category products retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving category products: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("data", new ArrayList<>());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    private Map<String, String> createCategoryMap(String id, String name) {
        Map<String, String> category = new HashMap<>();
        category.put("id", id);
        category.put("name", name);
        return category;
    }
}
