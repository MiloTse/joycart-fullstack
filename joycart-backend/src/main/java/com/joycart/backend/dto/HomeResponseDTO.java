package com.joycart.backend.dto;

import com.joycart.backend.dto.common.BannerInfo;
import com.joycart.backend.dto.common.LocationInfo;
import com.joycart.backend.model.Category;
import com.joycart.backend.model.Product;

import java.util.List;

/**
 * 主页响应DTO
 * 匹配前端Home页面的数据结构
 */
public class HomeResponseDTO {
    private boolean success;
    private HomeData data;

    public HomeResponseDTO() {
    }

    public HomeResponseDTO(boolean success, HomeData data) {
        this.success = success;
        this.data = data;
    }

    
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HomeData getData() {
        return data;
    }

    public void setData(HomeData data) {
        this.data = data;
    }

    /**
     * 主页数据内部类
     * 这个保留为内部类，因为它是HomeResponseDTO专用的数据结构
     */
    public static class HomeData {
        private LocationInfo location;
        private List<BannerInfo> banners;
        private List<Category> categories;
        private List<Product> fresh;

        public HomeData() {
        }

        public HomeData(LocationInfo location, List<BannerInfo> banners, 
                       List<Category> categories, List<Product> fresh) {
            this.location = location;
            this.banners = banners;
            this.categories = categories;
            this.fresh = fresh;
        }

        // Getters and Setters
        public LocationInfo getLocation() {
            return location;
        }

        public void setLocation(LocationInfo location) {
            this.location = location;
        }

        public List<BannerInfo> getBanners() {
            return banners;
        }

        public void setBanners(List<BannerInfo> banners) {
            this.banners = banners;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<Product> getFresh() {
            return fresh;
        }

        public void setFresh(List<Product> fresh) {
            this.fresh = fresh;
        }
    }
}

