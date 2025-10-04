package com.joycart.backend.service.impl;

import com.joycart.backend.model.Location;
import com.joycart.backend.repository.LocationRepository;
import com.joycart.backend.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {
    
    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Override
    public List<Map<String, String>> getAllActiveLocations() {
        logger.debug("Getting all active locations from database");
        
        try {
            List<Location> locations = locationRepository.findByIsActiveTrueOrderBySortOrderAscIdAsc();
            logger.info("Found {} active locations", locations.size());
            
            List<Map<String, String>> locationList = new ArrayList<>();
            for (Location location : locations) {
                Map<String, String> locationMap = new HashMap<>();
                locationMap.put("id", location.getLocationId());
                locationMap.put("name", location.getName());
                locationList.add(locationMap);
            }
            
            logger.info("Successfully retrieved location list");
            return locationList;
            
        } catch (Exception e) {
            logger.error("Error retrieving locations from database: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public Map<String, String> getLocationById(String locationId) {
        logger.debug("Getting location by id: {}", locationId);
        
        try {
            Location location = locationRepository.findByLocationIdAndIsActiveTrue(locationId);
            if (location == null) {
                logger.warn("Location not found for id: {}", locationId);
                return null;
            }
            
            Map<String, String> locationMap = new HashMap<>();
            locationMap.put("id", location.getLocationId());
            locationMap.put("name", location.getName());
            
            logger.info("Location retrieved successfully for id: {}", locationId);
            return locationMap;
            
        } catch (Exception e) {
            logger.error("Error retrieving location by id {}: {}", locationId, e.getMessage(), e);
            return null;
        }
    }
}
