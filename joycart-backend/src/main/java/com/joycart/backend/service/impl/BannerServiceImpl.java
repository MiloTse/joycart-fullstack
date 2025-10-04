package com.joycart.backend.service.impl;

import com.joycart.backend.model.Banner;
import com.joycart.backend.repository.BannerRepository;
import com.joycart.backend.service.BannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BannerServiceImpl implements BannerService {
    
    private static final Logger logger = LoggerFactory.getLogger(BannerServiceImpl.class);
    
    @Autowired
    private BannerRepository bannerRepository;
    
    @Override
    public List<Map<String, String>> getAllActiveBanners() {
        logger.debug("Getting all active banners from database");
        
        try {
            List<Banner> banners = bannerRepository.findByIsActiveTrueOrderBySortOrderAscIdAsc();
            logger.info("Found {} active banners", banners.size());
            
            List<Map<String, String>> bannerList = new ArrayList<>();
            for (Banner banner : banners) {
                Map<String, String> bannerMap = new HashMap<>();
                bannerMap.put("id", banner.getBannerId());
                bannerMap.put("imgUrl", banner.getImgUrl());
                bannerList.add(bannerMap);
            }
            
            logger.info("Successfully retrieved banner list");
            return bannerList;
            
        } catch (Exception e) {
            logger.error("Error retrieving banners from database: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public Map<String, String> getBannerById(String bannerId) {
        logger.debug("Getting banner by id: {}", bannerId);
        
        try {
            Banner banner = bannerRepository.findByBannerIdAndIsActiveTrue(bannerId);
            if (banner == null) {
                logger.warn("Banner not found for id: {}", bannerId);
                return null;
            }
            
            Map<String, String> bannerMap = new HashMap<>();
            bannerMap.put("id", banner.getBannerId());
            bannerMap.put("imgUrl", banner.getImgUrl());
            
            logger.info("Banner retrieved successfully for id: {}", bannerId);
            return bannerMap;
            
        } catch (Exception e) {
            logger.error("Error retrieving banner by id {}: {}", bannerId, e.getMessage(), e);
            return null;
        }
    }
}
