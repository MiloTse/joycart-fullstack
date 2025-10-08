package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getProductDetail(@PathVariable String id) {
        logger.info("Received product detail request for id: {}", id);
        
        try {
            // 从数据库获取商品详情数据
            Map<String, Object> productDetail = productService.getProductDetail(id);
            
            ResponseDTO<Map<String, Object>> response = ResponseDTO.success(ApiConstants.PRODUCT_DETAIL_SUCCESS_MESSAGE, productDetail);
            
            logger.info("Product detail retrieved successfully for id: {}", id);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving product detail for id: {} - {}", id, e.getMessage(), e);
            ResponseDTO<Map<String, Object>> errorResponse = ResponseDTO.error(ApiConstants.PRODUCT_DETAIL_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
