package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.joycart.backend.dto.ResponseDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @GetMapping("/hot")
    public ResponseEntity<ResponseDTO<List<Map<String, String>>>> getHotSearchList() {
        logger.info("Received hot search list request");
        
        try {
            // 硬编码热门搜索数据（模拟原始JSON）
            List<Map<String, String>> hotSearchList = Arrays.asList(
                createHotSearchItem("8318", "Pork"),
                createHotSearchItem("8317", "Steak"),
                createHotSearchItem("8319", "Seafood"),
                createHotSearchItem("8320", "Vegetables")
            );
            
            ResponseDTO<List<Map<String, String>>> response = ResponseDTO.success("热门搜索列表获取成功", hotSearchList);
            
            logger.info("Hot search list retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving hot search list: {}", e.getMessage(), e);
            ResponseDTO<List<Map<String, String>>> errorResponse = ResponseDTO.error("获取热门搜索列表失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 搜索商品列表
     * @param keyword 搜索关键词
     * @param shopId 店铺ID (可选参数)
     * @param type 搜索类型/排序方式 (可选参数: default, price, sales)
     * @return 商品搜索结果列表
     */
    @GetMapping("/products")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(required = false) String shopId,
            @RequestParam(required = false, defaultValue = "default") String type) {
        
        logger.info("Received product search request - keyword: {}, shopId: {}, type: {}", 
                   keyword, shopId, type);
        
        try {
            // 硬编码商品搜索数据（模拟原始JSON）
            List<Map<String, Object>> productList = Arrays.asList(
                createProductItem("88391", "/images/external/list-1.png", 
                    "Provolone cherry tomatoes natural seeds potato fruit vegetables healthy snack from Shandong Haian", 
                    49.8, 388),
                createProductItem("88392", "/images/external/list-2.png", 
                    "Natural sun-dried cherry tomatoes and potato fruit vegetables healthy snack from Shandong Xiwan", 
                    27.9, 982),
                createProductItem("88393", "/images/external/list-3.png", 
                    "Shandong Haiyang Provence Tomatoes Natural Ripe Sandy Tomatoes Fresh Vegetables", 
                    39.9, 546),
                createProductItem("88394", "/images/external/list-4.png", 
                    "Millennium Cherry Tomatoes Small Tomatoes 500g Premium Fresh Fruit", 
                    29.9, 368),
                createProductItem("88395", "/images/external/list-5.png", 
                    "Haiyang Provence Tomatoes Sandy Flesh 4.5kg Fresh Vegetables Premium 5kg Pack", 
                    39.9, 598)
            );
            
            ResponseDTO<List<Map<String, Object>>> response = ResponseDTO.success("商品搜索完成", productList);
            
            logger.info("Product search completed successfully, found {} products", productList.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error searching products: {}", e.getMessage(), e);
            ResponseDTO<List<Map<String, Object>>> errorResponse = ResponseDTO.error("商品搜索失败，请重试");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    private Map<String, String> createHotSearchItem(String id, String keyword) {
        Map<String, String> item = new HashMap<>();
        item.put("id", id);
        item.put("keyword", keyword);
        return item;
    }

    private Map<String, Object> createProductItem(String id, String imgUrl, String title, 
                                                  double price, int sales) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", id);
        item.put("imgUrl", imgUrl);
        item.put("title", title);
        item.put("price", price);
        item.put("sales", sales);
        return item;
    }
}
