package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.service.NearbyStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/nearby")
@CrossOrigin("*")
public class NearbyController {

    private static final Logger logger = LoggerFactory.getLogger(NearbyController.class);

    @Autowired
    private NearbyStoreService nearbyStoreService;

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
            // 从数据库获取附近商店数据
            List<Map<String, String>> storeList = nearbyStoreService.getAllActiveStores();
            
            if (storeList == null) {
                logger.warn("No stores found in database");
                return ResponseEntity.badRequest().body(ResponseDTO.error(ApiConstants.NEARBY_STORES_NOT_FOUND_MESSAGE));
            }
            
            logger.info("Nearby stores retrieved successfully from database, found {} stores", storeList.size());
            return ResponseEntity.ok(ResponseDTO.success(ApiConstants.NEARBY_STORES_SUCCESS_MESSAGE, storeList));
            
        } catch (Exception e) {
            logger.error("Error retrieving nearby stores: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error(ApiConstants.NEARBY_STORES_FAILED_MESSAGE + ": " + e.getMessage()));
        }
    }

}
