package com.joycart.backend.service.impl;

import com.joycart.backend.model.Product;
import com.joycart.backend.repository.ProductRepository;
import com.joycart.backend.service.ProductService;
import com.joycart.backend.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private TranslationService translationService;

    @Override
    public Map<String, Object> getProductDetail(String productId, String languageCode) {
        logger.debug("Getting product detail for productId: {}, languageCode: {}", productId, languageCode);
        
        try {
            Optional<Product> productOptional = productRepository.findByProductIdAndIsActiveTrue(productId);
            if (!productOptional.isPresent()) {
                logger.warn("Product not found for productId: {}", productId);
                return null;
            }
            
            Product product = productOptional.get();
            Map<String, Object> productDetail = new HashMap<>();
            
            // 基础信息
            productDetail.put("id", product.getProductId());
            productDetail.put("imgUrl", product.getImgUrl());
            productDetail.put("price", product.getPrice());
            productDetail.put("sales", product.getSales());
            productDetail.put("origin", product.getOrigin());
            productDetail.put("specification", product.getSpecification());
            productDetail.put("detail", product.getDetail());
            
            // 获取翻译后的标题和副标题
            String translatedTitle = translationService.getTranslationWithFallback("product", product.getId(), "title", languageCode);
            String translatedSubtitle = translationService.getTranslationWithFallback("product", product.getId(), "subtitle", languageCode);
            
            // 使用翻译文本，如果没有翻译则使用默认英文
            productDetail.put("title", translatedTitle != null ? translatedTitle : product.getTitle());
            productDetail.put("subtitle", translatedSubtitle != null ? translatedSubtitle : product.getSubtitle());
            
            logger.info("Product detail retrieved successfully for productId: {}, language: {}", productId, languageCode);
            return productDetail;
            
        } catch (Exception e) {
            logger.error("Error getting product detail for productId: {} - {}", productId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public List<Map<String, Object>> getAllActiveProducts(String languageCode) {
        logger.debug("Getting all active products, lang={}", languageCode);
        
        try {
            //fetch active products from database
            List<Product> products = productRepository.findByIsActiveTrue();
            logger.info("Found {} active products", products.size());
            
            List<Map<String, Object>> productList = new ArrayList<>();
            for (Product product : products) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getProductId());
                
                // 获取翻译后的标题和副标题
                String translatedTitle = translationService.getTranslationWithFallback(
                        "product", product.getId(), "title", languageCode);
                String translatedSubtitle = translationService.getTranslationWithFallback(
                        "product", product.getId(), "subtitle", languageCode);
                
                productMap.put("title", translatedTitle != null ? translatedTitle : product.getTitle());
                productMap.put("subtitle", translatedSubtitle != null ? translatedSubtitle : product.getSubtitle());
                productMap.put("imgUrl", product.getImgUrl());
                productMap.put("price", product.getPrice());
                productMap.put("sales", product.getSales());
                productList.add(productMap);
            }
            
            logger.info("Successfully retrieved {} active products", productList.size());
            return productList;
            
        } catch (Exception e) {
            logger.error("Error getting all active products: {}", e.getMessage(), e);
            return null;
        }
    }

}
