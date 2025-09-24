package com.joycart.backend.controller;

import com.joycart.backend.dto.ResponseDTO;
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
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getCategoryAndTagList() {
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
            
            logger.info("Category and tag list retrieved successfully");
            return ResponseEntity.ok(ResponseDTO.success("分类和标签列表获取成功", data));
            
        } catch (Exception e) {
            logger.error("Error retrieving category and tag list: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error("获取分类和标签列表失败: " + e.getMessage()));
        }
    }
    
    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> getCategoryProducts() {
        logger.info("Received category products request");
        
        try {
            // 硬编码商品列表数据（包含前端原有的chicken wing商品）
            List<Map<String, Object>> products = Arrays.asList(
                createMockProduct("1132381", "Domestic pork, skinless pork belly blocks", "/images/external/fresh-1.png", 66.9, 156),
                createMockProduct("1132382", "Prime live Boston lobster 2 pcs large package", "/images/external/fresh-2.png", 98.0, 89),
                createMockProduct("1132383", "Prime imported salmon 2 pcs large package", "/images/external/fresh-3.png", 378.0, 45),
                createMockProduct("1132384", "Fresh frozen squid head frozen squid tentacles 400g", "/images/external/fresh-4.png", 39.9, 203),
                createMockProduct("1132385", "chicken wing middle 1000g/...", "/images/external/fresh-1.png", 156.0, 156)
            );
            
            logger.info("Category products retrieved successfully");
            return ResponseEntity.ok(ResponseDTO.success("分类商品列表获取成功", products));
            
        } catch (Exception e) {
            logger.error("Error retrieving category products: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error("获取分类商品列表失败: " + e.getMessage()));
        }
    }
    
    private Map<String, String> createCategoryMap(String id, String name) {
        Map<String, String> category = new HashMap<>();
        category.put("id", id);
        category.put("name", name);
        return category;
    }
    
    private Map<String, Object> createMockProduct(String id, String title, String imgUrl, double price, int sales) {
        Map<String, Object> product = new HashMap<>();
        product.put("id", id);
        product.put("title", title);
        product.put("imgUrl", imgUrl);
        product.put("price", price);
        product.put("sales", sales);
        return product;
    }
}
