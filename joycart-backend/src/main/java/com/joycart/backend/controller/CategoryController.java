package com.joycart.backend.controller;

import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.CategoryService;
import com.joycart.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getCategoryAndTagList() {
        logger.info("Received category and tag list request");
        
        try {
            // 从数据库获取分类和标签数据
            Map<String, Object> data = categoryService.getCategoryAndTagList();
            
            if (data == null) {
                logger.warn("No category and tag data found in database");
                return ResponseEntity.badRequest().body(ResponseDTO.error("数据库中未找到分类和标签数据"));
            }
            
            logger.info("Category and tag list retrieved successfully from database");
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
            // 真实查询数据库数据，代替之前的硬编码
            List<Map<String, Object>> products = productService.getAllActiveProducts();
            
            if (products == null) {
                logger.warn("No products found in database");
                return ResponseEntity.badRequest().body(ResponseDTO.error("数据库中未找到商品数据"));
            }
            
            logger.info("Category products retrieved successfully from database");
            return ResponseEntity.ok(ResponseDTO.success("分类商品列表获取成功", products));
            
        } catch (Exception e) {
            logger.error("Error retrieving category products: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error("获取分类商品列表失败: " + e.getMessage()));
        }
    }
}
