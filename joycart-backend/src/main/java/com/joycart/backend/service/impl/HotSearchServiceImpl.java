package com.joycart.backend.service.impl;

import com.joycart.backend.model.HotSearch;
import com.joycart.backend.repository.HotSearchRepository;
import com.joycart.backend.service.HotSearchService;
import com.joycart.backend.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HotSearchServiceImpl implements HotSearchService {
    
    private static final Logger logger = LoggerFactory.getLogger(HotSearchServiceImpl.class);
    
    @Autowired
    private HotSearchRepository hotSearchRepository;
    
    @Autowired
    private TranslationService translationService;
    
    @Override
    public List<Map<String, String>> getAllActiveHotSearches(String languageCode) {
        logger.debug("Getting all active hot searches from database, lang={}", languageCode);
        
        try {
            List<HotSearch> hotSearches = hotSearchRepository.findByIsActiveTrueOrderBySortOrderAscIdAsc();
            logger.info("Found {} active hot searches", hotSearches.size());
            
            List<Map<String, String>> hotSearchList = new ArrayList<>();
            for (HotSearch hotSearch : hotSearches) {
                Map<String, String> hotSearchMap = new HashMap<>();
                hotSearchMap.put("id", hotSearch.getSearchId());
                
                // 获取翻译后的关键词
                String translatedKeyword = translationService.getTranslationWithFallback(
                        "hot_search", hotSearch.getId(), "keyword", languageCode);
                
                hotSearchMap.put("keyword", translatedKeyword != null ? translatedKeyword : hotSearch.getKeyword());
                hotSearchList.add(hotSearchMap);
            }
            
            logger.info("Successfully retrieved hot search list");
            return hotSearchList;
            
        } catch (Exception e) {
            logger.error("Error retrieving hot searches from database: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public Map<String, String> getHotSearchById(String searchId, String languageCode) {
        logger.debug("Getting hot search by id: {}, lang={}", searchId, languageCode);
        
        try {
            HotSearch hotSearch = hotSearchRepository.findBySearchIdAndIsActiveTrue(searchId);
            if (hotSearch == null) {
                logger.warn("Hot search not found for id: {}", searchId);
                return null;
            }
            
            Map<String, String> hotSearchMap = new HashMap<>();
            hotSearchMap.put("id", hotSearch.getSearchId());
            
            // 获取翻译后的关键词
            String translatedKeyword = translationService.getTranslationWithFallback(
                    "hot_search", hotSearch.getId(), "keyword", languageCode);
            
            hotSearchMap.put("keyword", translatedKeyword != null ? translatedKeyword : hotSearch.getKeyword());
            
            logger.info("Hot search retrieved successfully for id: {}", searchId);
            return hotSearchMap;
            
        } catch (Exception e) {
            logger.error("Error retrieving hot search by id {}: {}", searchId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    @Transactional
    public void incrementSearchCount(String keyword) {
        logger.debug("Incrementing search count for keyword: {}", keyword);
        
        try {
            HotSearch hotSearch = hotSearchRepository.findByKeywordAndIsActiveTrue(keyword);
            if (hotSearch != null) {
                hotSearch.setSearchCount(hotSearch.getSearchCount() + 1);
                hotSearchRepository.save(hotSearch);
                logger.info("Search count incremented for keyword: {}, new count: {}", keyword, hotSearch.getSearchCount());
            } else {
                logger.warn("Hot search not found for keyword: {}", keyword);
            }
        } catch (Exception e) {
            logger.error("Error incrementing search count for keyword {}: {}", keyword, e.getMessage(), e);
        }
    }
}
