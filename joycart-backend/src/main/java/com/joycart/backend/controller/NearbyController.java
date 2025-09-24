package com.joycart.backend.controller;

import com.joycart.backend.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nearby")
@CrossOrigin("*")
public class NearbyController {

    private static final Logger logger = LoggerFactory.getLogger(NearbyController.class);

    /**
     * 获取附近商店列表
     * @param latitude 用户当前纬度 (可选参数)
     * @param longitude 用户当前经度 (可选参数)
     * @return 附近商店列表
     */
    @GetMapping("/stores")
    public ResponseEntity<ResponseDTO<List<Map<String, String>>>> getNearbyStores(
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude) {
        
        logger.info("Received nearby stores request - latitude: {}, longitude: {}", 
                   latitude, longitude);
        
        try {
            // 硬编码附近商店数据（模拟原始JSON）
            List<Map<String, String>> storeList = Arrays.asList(
                createStoreItem("8318", "College Square Store", "1-613-001-9999", 
                    "1385 Woodroffe Ave, Ottawa, ON K4S 1A6, Canada", 
                    "799m", "45.3498", "-75.7553"),
                createStoreItem("8317", "Kanata Technology Park Store", "1-613-003-8888", 
                    "350 Legget Dr, Kanata, ON K2K 3N1, Canada", 
                    "1.1km", "45.333", "-75.7369"),
                createStoreItem("8319", "Rideau Centre Store", "1-613-005-7777", 
                    "50 Rideau St, Ottawa, ON K1N 9J7, Canada", 
                    "2.3km", "45.4215", "-75.6919"),
                createStoreItem("8320", "Bayshore Shopping Centre Store", "1-613-007-6666", 
                    "100 Bayshore Dr, Ottawa, ON K2B 8C1, Canada", 
                    "3.8km", "45.3636", "-75.8064"),
                createStoreItem("8321", "St. Laurent Shopping Centre Store", "1-613-009-5555", 
                    "1200 St Laurent Blvd, Ottawa, ON K1K 3B8, Canada", 
                    "4.2km", "45.4213", "-75.6187")
            );
            
            logger.info("Nearby stores retrieved successfully, found {} stores", storeList.size());
            return ResponseEntity.ok(ResponseDTO.success("附近商店列表获取成功", storeList));
            
        } catch (Exception e) {
            logger.error("Error retrieving nearby stores: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error("获取附近商店列表失败: " + e.getMessage()));
        }
    }

    private Map<String, String> createStoreItem(String id, String name, String phone, 
                                                String address, String distance, 
                                                String latitude, String longitude) {
        Map<String, String> item = new HashMap<>();
        item.put("id", id);
        item.put("name", name);
        item.put("phone", phone);
        item.put("address", address);
        item.put("distance", distance);
        item.put("latitude", latitude);
        item.put("longitude", longitude);
        return item;
    }
}
