package com.joycart.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("/hot")
    public ResponseEntity<?> getHotSearchList() {
        logger.info("Received hot search list request");
        
        try {
            // 硬编码热门搜索数据（模拟原始JSON）
            List<Map<String, String>> hotSearchList = Arrays.asList(
                createHotSearchItem("8318", "Pork"),
                createHotSearchItem("8317", "Steak"),
                createHotSearchItem("8319", "Seafood"),
                createHotSearchItem("8320", "Vegetables")
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "success");
            response.put("data", hotSearchList);
            
            logger.info("Hot search list retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving hot search list: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "error");
            errorResponse.put("data", Arrays.asList());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    private Map<String, String> createHotSearchItem(String id, String keyword) {
        Map<String, String> item = new HashMap<>();
        item.put("id", id);
        item.put("keyword", keyword);
        return item;
    }
}
