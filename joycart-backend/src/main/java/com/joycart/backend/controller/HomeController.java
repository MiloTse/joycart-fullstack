package com.joycart.backend.controller;

import com.joycart.backend.constants.ApiConstants;
import com.joycart.backend.dto.HomeResponseDTO;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.dto.common.BannerInfo;
import com.joycart.backend.dto.common.LocationInfo;
import com.joycart.backend.model.Category;
import com.joycart.backend.model.Product;
import com.joycart.backend.service.CategoryService;
import com.joycart.backend.service.ProductService;
import com.joycart.backend.service.LocationService;
import com.joycart.backend.service.BannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
@CrossOrigin("*")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private LocationService locationService;
    
    @Autowired
    private BannerService bannerService;

    @GetMapping
    public ResponseEntity<ResponseDTO<HomeResponseDTO.HomeData>> getHomeData() {
        logger.info("Received home data request");
        
        try {
            // 从数据库获取位置信息
            Map<String, String> locationData = locationService.getLocationById("001");
            LocationInfo location;
            if (locationData != null) {
                location = new LocationInfo(locationData.get("id"), locationData.get("name"));
            } else {
                // 如果数据库中没有位置数据，使用默认值
                logger.warn("No location data found in database, using default location");
                location = new LocationInfo("001", "Ottawa(Chinatown store)");
            }
            
            // 从数据库获取轮播图信息
            List<Map<String, String>> bannerData = bannerService.getAllActiveBanners();
            List<BannerInfo> banners;
            if (bannerData != null && !bannerData.isEmpty()) {
                banners = new ArrayList<>();
                for (Map<String, String> bannerMap : bannerData) {
                    banners.add(new BannerInfo(bannerMap.get("id"), bannerMap.get("imgUrl")));
                }
            } else {
                // 如果数据库中没有轮播图数据，使用默认值
                logger.warn("No banner data found in database, using default banners");
                banners = Arrays.asList(
                    new BannerInfo("1135", "/images/banner01.png"),
                    new BannerInfo("1136", "/images/external/banner.png")
                );
            }
            
            // 从数据库获取分类信息 (传递默认语言)
            Map<String, Object> categoryData = categoryService.getCategoryAndTagList(ApiConstants.DEFAULT_LANGUAGE);
            List<Category> categories = convertToCategoryList(categoryData);
            
            // 从数据库获取商品信息
            List<Map<String, Object>> productData = productService.getAllActiveProducts();
            List<Product> freshProducts = convertToProductList(productData);
            
            // 构造响应数据
            HomeResponseDTO.HomeData homeData = new HomeResponseDTO.HomeData(
                location, banners, categories, freshProducts
            );
            
            logger.info("Home data retrieved successfully from database");
            return ResponseEntity.ok(ResponseDTO.success(ApiConstants.HOME_DATA_SUCCESS_MESSAGE, homeData));
            
        } catch (Exception e) {
            logger.error("Error retrieving home data: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error(ApiConstants.HOME_DATA_FAILED_MESSAGE + ": " + e.getMessage()));
        }
    }
    
    private List<Category> convertToCategoryList(Map<String, Object> categoryData) {
        if (categoryData == null) {
            logger.warn("No category data found");
            return Arrays.asList();
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, String>> categoryList = (List<Map<String, String>>) categoryData.get("categories");
        if (categoryList == null) {
            logger.warn("No categories found in category data");
            return Arrays.asList();
        }
        
        List<Category> categories = new java.util.ArrayList<>();
        // 只取前8个分类，对应原始Home页面的2行4列布局
        int maxCategories = Math.min(8, categoryList.size());
        for (int i = 0; i < maxCategories; i++) {
            Map<String, String> categoryMap = categoryList.get(i);
            Category category = new Category();
            category.setId(Long.parseLong(categoryMap.get("id")));
            category.setName(categoryMap.get("name"));
            category.setImgUrl(categoryMap.get("imgUrl")); // 使用数据库中的图片URL
            category.setDescription("");
            category.setActive(true);
            categories.add(category);
        }
        
        logger.info("Converted {} categories for home display", categories.size());
        return categories;
    }
    
    private List<Product> convertToProductList(List<Map<String, Object>> productData) {
        if (productData == null) {
            logger.warn("No product data found");
            return Arrays.asList();
        }
        
        List<Product> products = new java.util.ArrayList<>();
        for (Map<String, Object> productMap : productData) {
            Product product = new Product();
            product.setId(Long.parseLong(productMap.get("id").toString()));
            product.setProductId(productMap.get("id").toString());
            product.setTitle(productMap.get("title").toString());
            product.setSubtitle(productMap.get("subtitle") != null ? productMap.get("subtitle").toString() : "");
            product.setPrice(BigDecimal.valueOf(Double.parseDouble(productMap.get("price").toString())));
            product.setImgUrl(productMap.get("imgUrl").toString());
            product.setSales(Integer.parseInt(productMap.get("sales").toString()));
            product.setIsActive(true);
            products.add(product);
        }
        
        return products;
    }
}
