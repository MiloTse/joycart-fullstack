package com.joycart.backend.service.impl;

import com.joycart.backend.model.Product;
import com.joycart.backend.repository.ProductRepository;
import com.joycart.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Map<String, Object> getProductDetail(String productId) {
        logger.debug("Getting product detail for productId: {}", productId);
        
        try {
            Optional<Product> productOptional = productRepository.findByProductIdAndIsActiveTrue(productId);
            if (!productOptional.isPresent()) {
                logger.warn("Product not found for productId: {}", productId);
                return null;
            }
            
            Product product = productOptional.get();
            Map<String, Object> productDetail = new HashMap<>();
            
            productDetail.put("id", product.getProductId());
            productDetail.put("imgUrl", product.getImgUrl());
            productDetail.put("title", product.getTitle());
            productDetail.put("subtitle", product.getSubtitle());
            productDetail.put("price", product.getPrice());
            productDetail.put("sales", product.getSales());
            productDetail.put("origin", product.getOrigin());
            productDetail.put("specification", product.getSpecification());
            productDetail.put("detail", product.getDetail());
            
            logger.info("Product detail retrieved successfully for productId: {}", productId);
            return productDetail;
            
        } catch (Exception e) {
            logger.error("Error getting product detail for productId: {} - {}", productId, e.getMessage(), e);
            return null;
        }
    }
    

}
