package com.joycart.backend.constants;

/**
 * API相关常量
 */
public class ApiConstants {
    
    /**
     * HTTP请求头常量
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    
    /**
     * JWT相关常量
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    
    /**
     * 响应消息常量
     */
    public static final String SUCCESS_MESSAGE = "操作成功";
    public static final String ERROR_MESSAGE = "操作失败";
    public static final String UNAUTHORIZED_MESSAGE = "用户认证失败";
    public static final String DATA_NOT_FOUND_MESSAGE = "数据未找到";
    
    /**
     * 用户相关常量
     */
    public static final String USER_NOT_FOUND_MESSAGE = "用户不存在";
    public static final String INVALID_TOKEN_MESSAGE = "无效的token";
    
    /**
     * 商品相关常量
     */
    public static final String PRODUCT_NOT_FOUND_MESSAGE = "商品不存在";
    public static final String PRODUCT_LIST_SUCCESS_MESSAGE = "商品列表获取成功";
    
    /**
     * 分类相关常量
     */
    public static final String CATEGORY_LIST_SUCCESS_MESSAGE = "分类列表获取成功";
    public static final String CATEGORY_DATA_NOT_FOUND_MESSAGE = "数据库中未找到分类和标签数据";
    
    /**
     * 购物车相关常量
     */
    public static final String CART_ITEM_COUNT_SUCCESS_MESSAGE = "购物车商品数量获取成功";
    public static final String CART_ADD_SUCCESS_MESSAGE = "商品添加到购物车成功";
    public static final String CART_CHANGE_SUCCESS_MESSAGE = "购物车商品修改成功";
    public static final String CART_PRODUCTS_SUCCESS_MESSAGE = "购物车商品列表获取成功";
    
    /**
     * 订单相关常量
     */
    public static final String ORDER_SUBMIT_SUCCESS_MESSAGE = "订单提交成功";
    public static final String ORDER_DETAIL_SUCCESS_MESSAGE = "订单详情获取成功";
    public static final String ORDER_PAYMENT_SUCCESS_MESSAGE = "支付处理成功";
    public static final String USER_ADDRESSES_SUCCESS_MESSAGE = "用户地址列表获取成功";
    public static final String USER_ADDRESSES_NOT_FOUND_MESSAGE = "数据库中未找到用户地址数据";
    
    /**
     * 搜索相关常量
     */
    public static final String HOT_SEARCH_SUCCESS_MESSAGE = "热门搜索列表获取成功";
    public static final String PRODUCT_SEARCH_SUCCESS_MESSAGE = "商品搜索完成";
    
    /**
     * 附近商店相关常量
     */
    public static final String NEARBY_STORES_SUCCESS_MESSAGE = "附近商店列表获取成功";
    public static final String NEARBY_STORES_NOT_FOUND_MESSAGE = "数据库中未找到商店数据";
    
    /**
     * 主页相关常量
     */
    public static final String HOME_DATA_SUCCESS_MESSAGE = "主页数据获取成功";
}
