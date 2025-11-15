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

import static com.joycart.backend.constants.ApiConstants.PRODUCT_DETAIL;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_ID;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_IMG_URL;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_ORIGIN;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_PRICE;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_SALES;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_SPECIFICATION;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_SUBTITLE;
import static com.joycart.backend.constants.ApiConstants.PRODUCT_TITLE;
import static com.joycart.backend.constants.ApiConstants.TRANSLATIONS_FIELD_DETAIL;
import static com.joycart.backend.constants.ApiConstants.TRANSLATIONS_FIELD_ORIGIN;
import static com.joycart.backend.constants.ApiConstants.TRANSLATIONS_FIELD_SPECIFICATION;
import static com.joycart.backend.constants.ApiConstants.TRANSLATIONS_FIELD_SUBTITLE;
import static com.joycart.backend.constants.ApiConstants.TRANSLATIONS_FIELD_TITLE;
import static com.joycart.backend.constants.ApiConstants.TRANSLATIONS_TYPE_PRODUCT;

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
            productDetail.put(PRODUCT_ID, product.getProductId());
            productDetail.put(PRODUCT_IMG_URL, product.getImgUrl());
            productDetail.put(PRODUCT_PRICE, product.getPrice());
            productDetail.put(PRODUCT_SALES, product.getSales());
            
            // 获取翻译后的标题和副标题
            String translatedTitle = translationService.getTranslationWithFallback(TRANSLATIONS_TYPE_PRODUCT, product.getId(), TRANSLATIONS_FIELD_TITLE, languageCode);
            String translatedSubtitle = translationService.getTranslationWithFallback(TRANSLATIONS_TYPE_PRODUCT, product.getId(), TRANSLATIONS_FIELD_SUBTITLE, languageCode);
            String translatedOrigin = translationService.getTranslationWithFallback(TRANSLATIONS_TYPE_PRODUCT, product.getId(), TRANSLATIONS_FIELD_ORIGIN, languageCode);
            String translatedSpecification = translationService.getTranslationWithFallback(TRANSLATIONS_TYPE_PRODUCT, product.getId(), TRANSLATIONS_FIELD_SPECIFICATION, languageCode);
            String translatedDetail = translationService.getTranslationWithFallback(TRANSLATIONS_TYPE_PRODUCT, product.getId(), TRANSLATIONS_FIELD_DETAIL, languageCode);
            
            // 使用翻译文本，如果没有翻译则使用默认英文
            productDetail.put(PRODUCT_TITLE, translatedTitle != null ? translatedTitle : product.getTitle());
            productDetail.put(PRODUCT_SUBTITLE, translatedSubtitle != null ? translatedSubtitle : product.getSubtitle());
            productDetail.put(PRODUCT_ORIGIN, translatedOrigin != null ? translatedOrigin : product.getOrigin());
            productDetail.put(PRODUCT_SPECIFICATION, translatedSpecification != null ? translatedSpecification : product.getSpecification());
            productDetail.put(PRODUCT_DETAIL, translatedDetail != null ? translatedDetail : product.getDetail());
            
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
                productMap.put(PRODUCT_ID, product.getProductId());
                
                // 获取翻译后的标题和副标题
                String translatedTitle = translationService.getTranslationWithFallback(
                        TRANSLATIONS_TYPE_PRODUCT, product.getId(), TRANSLATIONS_FIELD_TITLE, languageCode);
                String translatedSubtitle = translationService.getTranslationWithFallback(
                        TRANSLATIONS_TYPE_PRODUCT, product.getId(), TRANSLATIONS_FIELD_SUBTITLE, languageCode);
                
                productMap.put(PRODUCT_TITLE, translatedTitle != null ? translatedTitle : product.getTitle());
                productMap.put(PRODUCT_SUBTITLE, translatedSubtitle != null ? translatedSubtitle : product.getSubtitle());
                productMap.put(PRODUCT_IMG_URL, product.getImgUrl());
                productMap.put(PRODUCT_PRICE, product.getPrice());
                productMap.put(PRODUCT_SALES, product.getSales());
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
