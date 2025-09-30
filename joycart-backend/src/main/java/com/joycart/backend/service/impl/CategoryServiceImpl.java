package com.joycart.backend.service.impl;

import com.joycart.backend.model.Category;
import com.joycart.backend.model.Tag;
import com.joycart.backend.repository.CategoryRepository;
import com.joycart.backend.repository.TagRepository;
import com.joycart.backend.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public Map<String, Object> getCategoryAndTagList() {
        logger.debug("Getting category and tag list from database");
        
        try {
            // 获取激活的分类列表
            List<Category> categories = categoryRepository.findAllActiveOrderBySortOrder();
            logger.info("Found {} active categories", categories.size());
            
            // 获取激活的标签列表
            List<Tag> tags = tagRepository.findAllActiveOrderBySortOrder();
            logger.info("Found {} active tags", tags.size());
            
            // 如果数据库中没有数据，返回默认数据（保持向后兼容）
            if (categories.isEmpty() && tags.isEmpty()) {
                logger.info("No data found in database, returning default data");
                return createDefaultCategoryAndTagData();
            }
            
            // 构建返回数据 - 转换为前端期望的格式
            Map<String, Object> result = new HashMap<>();
            
            // 转换Category实体为前端期望的格式
            List<Map<String, String>> categoryList = new ArrayList<>();
            for (Category category : categories) {
                Map<String, String> categoryMap = new HashMap<>();
                categoryMap.put("id", String.valueOf(category.getId()));
                categoryMap.put("name", category.getName());
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
            // 发生异常时也返回默认数据，保证API稳定性
            return createDefaultCategoryAndTagData();
        }
    }
    
    private Map<String, Object> createDefaultCategoryAndTagData() {
        Map<String, Object> result = new HashMap<>();
        
        // 默认分类数据（恢复原始categoryAndTagList.json数据）
        List<Map<String, String>> defaultCategories = Arrays.asList(
            createCategoryMap("8210", "精选商品"),
            createCategoryMap("8211", "单品优惠"),
            createCategoryMap("8212", "新鲜水果"),
            createCategoryMap("8213", "时令蔬菜"),
            createCategoryMap("8214", "肉蛋家禽"),
            createCategoryMap("8215", "水产海鲜"),
            createCategoryMap("8216", "牛奶面包"),
            createCategoryMap("8217", "冷冻冷藏"),
            createCategoryMap("8218", "米面粮油"),
            createCategoryMap("8219", "生活用品"),
            createCategoryMap("8220", "进口食品"),
            createCategoryMap("8221", "精美礼盒")
        );
        
        // 默认标签数据（恢复原始categoryAndTagList.json数据）
        List<String> defaultTags = Arrays.asList(
            "果蔬", "肉蛋家禽", "海鲜"
        );
        
        result.put("categories", defaultCategories);
        result.put("tags", defaultTags);
        
        logger.info("Returning default category and tag data");
        return result;
    }
    
    private Map<String, String> createCategoryMap(String id, String name) {
        Map<String, String> category = new HashMap<>();
        category.put("id", id);
        category.put("name", name);
        return category;
    }

}
