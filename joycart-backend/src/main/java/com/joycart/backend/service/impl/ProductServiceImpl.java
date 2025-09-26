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
                // 返回默认商品数据（保持与原有硬编码逻辑一致）
                return createDefaultProductDetail(productId);
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
            // 发生异常时返回默认商品数据
            return createDefaultProductDetail(productId);
        }
    }
    

    private Map<String, Object> createDefaultProductDetail(String productId) {
        Map<String, Object> product = new HashMap<>();
        product.put("id", productId);
        product.put("imgUrl", "/images/external/detail.png");
        product.put("title", "Shandong Haiyang Provence Tomatoes - Natural Ripe Sandy Tomatoes Fresh Fruit Vegetables Healthy Food Premium Box 5kg");
        product.put("subtitle", "Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce");
        product.put("price", 39.9);
        product.put("sales", 456);
        product.put("origin", "Based on actual purchased product batch");
        product.put("specification", "2kg");
        product.put("detail", "Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.");
        return product;
    }
}
