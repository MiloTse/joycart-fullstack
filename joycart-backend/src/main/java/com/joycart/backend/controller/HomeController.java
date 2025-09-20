package com.joycart.backend.controller;

import com.joycart.backend.dto.HomeResponseDTO;
import com.joycart.backend.dto.common.BannerInfo;
import com.joycart.backend.dto.common.LocationInfo;
import com.joycart.backend.model.Category;
import com.joycart.backend.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/home")
@CrossOrigin("*")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    // 暂时移除数据库依赖，使用硬编码数据测试

    @GetMapping
    public ResponseEntity<HomeResponseDTO> getHomeData() {
        logger.info("Received home data request");
        
        try {
            // 获取位置信息（暂时硬编码）
            LocationInfo location = new LocationInfo("001", "Ottawa(Chinatown store)");
            
            // 获取轮播图信息（暂时硬编码，后续可以从数据库获取）
            List<BannerInfo> banners = Arrays.asList(
                new BannerInfo("1135", "/images/banner01.png"),
                new BannerInfo("1136", "/images/external/banner.png")
            );
            
            // 硬编码分类信息（模拟原始JSON数据）
            List<Category> categories = Arrays.asList(
                createMockCategory(113231L, "Produce", "/images/external/category-1.png"),
                createMockCategory(113232L, "Meat & Seafood", "/images/external/category-2.png"),
                createMockCategory(113233L, "Fresh Fruit", "/images/external/category-3.png"),
                createMockCategory(113234L, "Milk & Dairy", "/images/external/category-4.png")
            );
            
            // 硬编码新鲜商品信息（模拟原始JSON数据）
            List<Product> freshProducts = Arrays.asList(
                createMockProduct(1132381L, "Domestic pork, skinless pork belly blocks", "/images/external/fresh-1.png", "66.9"),
                createMockProduct(1132382L, "Prime live Boston lobster 2 pcs large package", "/images/external/fresh-2.png", "98")
            );
            
            // 构造响应数据
            HomeResponseDTO.HomeData homeData = new HomeResponseDTO.HomeData(
                location, banners, categories, freshProducts
            );
            
            HomeResponseDTO response = new HomeResponseDTO(true, homeData);
            
            logger.info("Home data retrieved successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error retrieving home data: {}", e.getMessage(), e);
            HomeResponseDTO errorResponse = new HomeResponseDTO(false, null);
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    private Category createMockCategory(Long id, String name, String imgUrl) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setImgUrl(imgUrl);
        category.setDescription("");
        category.setActive(true);
        return category;
    }
    
    private Product createMockProduct(Long id, String name, String imgUrl, String price) {
        Product product = new Product(name, new BigDecimal(price), imgUrl, "", 1L);
        product.setId(id);
        return product;
    }
}
