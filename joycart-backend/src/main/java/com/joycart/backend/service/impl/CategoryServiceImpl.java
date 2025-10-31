package com.joycart.backend.service.impl;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.model.Category;
import com.joycart.backend.model.Tag;
import com.joycart.backend.repository.CategoryRepository;
import com.joycart.backend.repository.TagRepository;
import com.joycart.backend.service.CategoryService;
import com.joycart.backend.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TranslationService translationService;

    @Override
    public Map<String, Object> getCategoryAndTagList(String languageCode) {
        logger.debug("Getting category and tag list from database, lang={}", languageCode);
        
        try {
            // 获取激活的分类列表
            List<Category> categories = categoryRepository.findAllActiveOrderBySortOrder();
            logger.info("Found {} active categories", categories.size());
            
            // 获取激活的标签列表
            List<Tag> tags = tagRepository.findAllActiveOrderBySortOrder();
            logger.info("Found {} active tags", tags.size());
            
            // 构建返回数据 - 转换为前端期望的格式
            Map<String, Object> result = new HashMap<>();
            
            // 转换Category实体为前端期望的格式（应用翻译）
            List<Map<String, String>> categoryList = new ArrayList<>();
            for (Category category : categories) {
                Map<String, String> categoryMap = new HashMap<>();
                categoryMap.put("id", String.valueOf(category.getId()));
                
                String translatedName = translationService.getTranslationWithFallback(
                        "category", category.getId(), "name", languageCode);
                String translatedDesc = translationService.getTranslationWithFallback(
                        "category", category.getId(), "description", languageCode);
                
                categoryMap.put("name", translatedName != null ? translatedName : category.getName());
                categoryMap.put("description", translatedDesc != null ? translatedDesc : (category.getDescription() == null ? "" : category.getDescription()));
                categoryMap.put("imgUrl", category.getImgUrl());
                categoryList.add(categoryMap);
            }
            
            // 转换Tag实体为前端期望的格式
            List<String> tagList = new ArrayList<>();
            for (Tag tag : tags) {
                tagList.add(tag.getName());
            }
            
            result.put("categories", categoryList);
            result.put("tags", tagList);
            
            logger.info("Successfully retrieved category and tag list");
            return result;
            
        } catch (Exception e) {
            logger.error("Error getting category and tag list: {}", e.getMessage(), e);
            return null;
        }
    }
}
