package com.joycart.backend.dto.common;

/**
 * Banner/图片信息DTO
 * 可复用于：主页轮播、商品图片、活动banner、分类推广图等
 */
public class BannerInfo {
    private String id;
    private String imgUrl;      // 图片URL
    private String title;       // 标题/描述
    private String linkUrl;     // 点击跳转链接
    private String type;        // banner类型：carousel/promotion/product/category
    private Integer sortOrder;  // 排序顺序
    private boolean isActive;   // 是否激活


    public BannerInfo() {
    }

    public BannerInfo(String id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.isActive = true;
    }

    public BannerInfo(String id, String imgUrl, String title, String linkUrl, String type) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.title = title;
        this.linkUrl = linkUrl;
        this.type = type;
        this.isActive = true;
    }

   
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
