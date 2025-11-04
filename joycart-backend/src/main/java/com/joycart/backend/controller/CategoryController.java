package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
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
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getCategoryAndTagList(
            @RequestParam(value = "lang", defaultValue = ApiConstants.DEFAULT_LANGUAGE) String languageCode) {
        logger.info("Received category and tag list request, lang={}", languageCode);
        
        try {
            // 从数据库获取分类和标签数据
            Map<String, Object> data = categoryService.getCategoryAndTagList(languageCode);
            
            if (data == null) {
                logger.warn("No category and tag data found in database");
                return ResponseEntity.badRequest().body(ResponseDTO.error(ApiConstants.CATEGORY_DATA_NOT_FOUND_MESSAGE));
            }
            
            logger.info("Category and tag list retrieved successfully from database");
            return ResponseEntity.ok(ResponseDTO.success(ApiConstants.CATEGORY_LIST_SUCCESS_MESSAGE, data));
            
        } catch (Exception e) {
            logger.error("Error retrieving category and tag list: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error(ApiConstants.ERROR_MESSAGE + ": " + e.getMessage()));
        }
    }
    
    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> getCategoryProducts() {
        logger.info("Received category products request");
        
        try {
            // 真实查询数据库数据，代替之前的硬编码 (使用默认语言)
            List<Map<String, Object>> products = productService.getAllActiveProducts(ApiConstants.DEFAULT_LANGUAGE);
            
            if (products == null) {
                logger.warn("No products found in database");
                return ResponseEntity.badRequest().body(ResponseDTO.error(ApiConstants.PRODUCT_DATA_NOT_FOUND_MESSAGE));
            }
            
            logger.info("Category products retrieved successfully from database");
            return ResponseEntity.ok(ResponseDTO.success(ApiConstants.CATEGORY_PRODUCTS_SUCCESS_MESSAGE, products));
            
        } catch (Exception e) {
            logger.error("Error retrieving category products: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error(ApiConstants.CATEGORY_PRODUCTS_FAILED_MESSAGE + ": " + e.getMessage()));
        }
    }
}
