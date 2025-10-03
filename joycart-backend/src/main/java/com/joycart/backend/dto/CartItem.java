package com.joycart.backend.dto;

/**
 * 购物车商品项数据结构
 */
public class CartItem {
    private String productId;
    private Integer count;

    public CartItem() {}
    
    public CartItem(String productId, Integer count) {
        this.productId = productId;
        this.count = count;
    }

    public String getProductId() { 
        return productId; 
    }
    
    public void setProductId(String productId) { 
        this.productId = productId; 
    }
    
    public Integer getCount() { 
        return count; 
    }
    
    public void setCount(Integer count) { 
        this.count = count; 
    }
    
    @Override
    public String toString() {
        return "CartItem{productId='" + productId + "', count=" + count + "}";
    }
}
