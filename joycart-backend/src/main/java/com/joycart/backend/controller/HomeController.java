package com.joycart.backend.controller;

import com.joycart.backend.dto.HomeResponseDTO;
import com.joycart.backend.dto.ResponseDTO;
import com.joycart.backend.dto.common.BannerInfo;
import com.joycart.backend.dto.common.LocationInfo;
import com.joycart.backend.model.Category;
import com.joycart.backend.model.Product;
import com.joycart.backend.service.CategoryService;
import com.joycart.backend.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ResponseDTO<HomeResponseDTO.HomeData>> getHomeData() {
        logger.info("Received home data request");
        
        try {
            // 获取位置信息（暂时硬编码）
            LocationInfo location = new LocationInfo("001", "Ottawa(Chinatown store)");
            
            // 获取轮播图信息（暂时硬编码，后续可以从数据库获取）
            List<BannerInfo> banners = Arrays.asList(
                new BannerInfo("1135", "/images/banner01.png"),
                new BannerInfo("1136", "/images/external/banner.png")
            );
            
            // 从数据库获取分类信息
            Map<String, Object> categoryData = categoryService.getCategoryAndTagList();
            List<Category> categories = convertToCategoryList(categoryData);
            
            // 从数据库获取商品信息
            List<Map<String, Object>> productData = productService.getAllActiveProducts();
            List<Product> freshProducts = convertToProductList(productData);
            
            // 构造响应数据
            HomeResponseDTO.HomeData homeData = new HomeResponseDTO.HomeData(
                location, banners, categories, freshProducts
            );
            
            logger.info("Home data retrieved successfully from database");
            return ResponseEntity.ok(ResponseDTO.success("主页数据获取成功", homeData));
            
        } catch (Exception e) {
            logger.error("Error retrieving home data: {}", e.getMessage(), e);
            return ResponseEntity.ok(ResponseDTO.error("主页数据获取失败: " + e.getMessage()));
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
        for (Map<String, String> categoryMap : categoryList) {
            Category category = new Category();
            category.setId(Long.parseLong(categoryMap.get("id")));
            category.setName(categoryMap.get("name"));
            category.setImgUrl("/images/external/category-1.png"); // 默认图片
            category.setDescription("");
            category.setActive(true);
            categories.add(category);
        }
        
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
            product.setPrice(Double.parseDouble(productMap.get("price").toString()));
            product.setImgUrl(productMap.get("imgUrl").toString());
            product.setSales(Integer.parseInt(productMap.get("sales").toString()));
            product.setIsActive(true);
            products.add(product);
        }
        
        return products;
    }
}
