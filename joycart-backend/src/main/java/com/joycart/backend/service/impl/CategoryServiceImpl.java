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
            
            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("categories", categories);
            result.put("tags", tags);
            
            logger.info("Successfully retrieved category and tag list");
            return result;
            
        } catch (Exception e) {
            logger.error("Error getting category and tag list: {}", e.getMessage(), e);
            throw e;
        }
    }

}
