package com.joycart.backend.service.impl;

import com.joycart.backend.model.NearbyStore;
import com.joycart.backend.repository.NearbyStoreRepository;
import com.joycart.backend.service.NearbyStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NearbyStoreServiceImpl implements NearbyStoreService {

    private static final Logger logger = LoggerFactory.getLogger(NearbyStoreServiceImpl.class);

    @Autowired
    private NearbyStoreRepository nearbyStoreRepository;

    @Override
    public List<Map<String, String>> getAllActiveStores() {
        logger.debug("Getting all active nearby stores from database");
        
        try {
            List<NearbyStore> stores = nearbyStoreRepository.findByIsActiveTrueOrderBySortOrderAsc();
            logger.info("Found {} active stores", stores.size());
            
            List<Map<String, String>> storeList = new ArrayList<>();
            for (NearbyStore store : stores) {
                Map<String, String> storeMap = new HashMap<>();
                storeMap.put("id", store.getStoreId());
                storeMap.put("name", store.getName());
                storeMap.put("phone", store.getPhone());
                storeMap.put("address", store.getAddress());
                storeMap.put("distance", store.getDistance());
                storeMap.put("latitude", store.getLatitude() != null ? store.getLatitude().toString() : "");
                storeMap.put("longitude", store.getLongitude() != null ? store.getLongitude().toString() : "");
                storeList.add(storeMap);
            }
            
            logger.info("Successfully retrieved {} active stores", storeList.size());
            return storeList;
            
        } catch (Exception e) {
            logger.error("Error getting all active stores: {}", e.getMessage(), e);
            return null;
        }
    }
}
