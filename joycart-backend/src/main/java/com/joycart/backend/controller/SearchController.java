package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.ProductService;
import com.joycart.backend.service.HotSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin("*")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ProductService productService;
    
    @Autowired
    private HotSearchService hotSearchService;

    @GetMapping("/hot")
    public ResponseEntity<ResponseDTO<List<Map<String, String>>>> getHotSearchList() {
        logger.info("Received hot search list request");
        
        try {
            // 从数据库获取热门搜索列表
            List<Map<String, String>> hotSearchList = hotSearchService.getAllActiveHotSearches();
            
            if (hotSearchList == null || hotSearchList.isEmpty()) {
                logger.warn("No hot search data found in database, using default data");
                // 如果数据库中没有数据，使用默认值
                hotSearchList = Arrays.asList(
                    createHotSearchItem("8318", "Pork"),
                    createHotSearchItem("8317", "Steak"),
                    createHotSearchItem("8319", "Seafood"),
                    createHotSearchItem("8320", "Vegetables")
                );
            }
            
            ResponseDTO<List<Map<String, String>>> response = ResponseDTO.success(ApiConstants.HOT_SEARCH_SUCCESS_MESSAGE, hotSearchList);
            
            logger.info("Hot search list retrieved successfully from database");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving hot search list: {}", e.getMessage(), e);
            ResponseDTO<List<Map<String, String>>> errorResponse = ResponseDTO.error(ApiConstants.HOT_SEARCH_FAILED_MESSAGE);
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
            // 从数据库获取所有商品进行搜索（简化实现，实际项目中应该实现真正的搜索逻辑）
            List<Map<String, Object>> allProducts = productService.getAllActiveProducts();
            
            if (allProducts == null) {
                logger.warn("No products found in database");
                return ResponseEntity.badRequest().body(ResponseDTO.error(ApiConstants.PRODUCT_DATA_NOT_FOUND_MESSAGE));
            }
            
            // 简单的关键词过滤（实际项目中应该使用更复杂的搜索算法）
            List<Map<String, Object>> filteredProducts = new java.util.ArrayList<>();
            for (Map<String, Object> product : allProducts) {
                String title = product.get("title").toString().toLowerCase();
                if (title.contains(keyword.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
            
            ResponseDTO<List<Map<String, Object>>> response = ResponseDTO.success(ApiConstants.PRODUCT_SEARCH_SUCCESS_MESSAGE, filteredProducts);
            
            logger.info("Product search completed successfully, found {} products for keyword: {}", 
                       filteredProducts.size(), keyword);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error searching products: {}", e.getMessage(), e);
            ResponseDTO<List<Map<String, Object>>> errorResponse = ResponseDTO.error(ApiConstants.PRODUCT_SEARCH_FAILED_MESSAGE);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    private Map<String, String> createHotSearchItem(String id, String keyword) {
        Map<String, String> item = new HashMap<>();
        item.put("id", id);
        item.put("keyword", keyword);
        return item;
    }

}
