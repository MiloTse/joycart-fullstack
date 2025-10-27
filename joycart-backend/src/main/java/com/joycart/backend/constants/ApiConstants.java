package com.joycart.backend.constants;

/**
 * API相关常量
 * 统一管理前后端共用的字段名和常量值
 */
public class ApiConstants {
    
    // ===========================================
    // Language Codes (ISO 639-1 + BCP 47)
    // ===========================================
    /** 英语 (美国) - 默认语言 */
    public static final String LANGUAGE_EN_US = "en-US";
    /** 中文 (简体) */
    public static final String LANGUAGE_ZH_CN = "zh-CN";
    /** 法语 (法国) */
    public static final String LANGUAGE_FR_FR = "fr-FR";
    /** 默认语言代码 */
    public static final String DEFAULT_LANGUAGE = LANGUAGE_EN_US;
    
    // ===========================================
    // HTTP请求头常量
    // ===========================================
    
    /**
     * HTTP请求头常量
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";
    
    /**
     * 响应字段名常量
     */
    public static final String RESPONSE_CODE = "code";
    public static final String RESPONSE_MESSAGE = "message";
    public static final String RESPONSE_DATA = "data";
    public static final String RESPONSE_SUCCESS = "success";
    
    /**
     * 用户相关字段名
     */
    public static final String USER_ID = "id";
    public static final String USER_NICKNAME = "nickname";
    public static final String USER_AVATAR = "avatar";
    public static final String USER_VIP_LEVEL = "vipLevel";
    public static final String USER_COUPONS = "coupons";
    public static final String USER_REWARD_POINTS = "rewardPoints";
    public static final String USER_PHONE_NUMBER = "phoneNumber";
    public static final String USER_EMAIL = "email";
    
    /**
     * 商品相关字段名
     */
    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_PRODUCT_ID = "productId";
    public static final String PRODUCT_TITLE = "title";
    public static final String PRODUCT_SUBTITLE = "subtitle";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_IMG_URL = "imgUrl";
    public static final String PRODUCT_SALES = "sales";
    public static final String PRODUCT_ORIGIN = "origin";
    public static final String PRODUCT_SPECIFICATION = "specification";
    public static final String PRODUCT_DETAIL = "detail";
    public static final String PRODUCT_IS_ACTIVE = "isActive";
    
    /**
     * 分类相关字段名
     */
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_IMG_URL = "imgUrl";
    public static final String CATEGORY_DESCRIPTION = "description";
    public static final String CATEGORY_SORT_ORDER = "sortOrder";
    public static final String CATEGORY_ACTIVE = "active";
    
    /**
     * 购物车相关字段名
     */
    public static final String CART_PRODUCT_ID = "productId";
    public static final String CART_COUNT = "count";
    public static final String CART_TITLE = "title";
    public static final String CART_PRICE = "price";
    public static final String CART_IMG_URL = "imgUrl";
    public static final String CART_SHOP_NAME = "shopName";
    public static final String CART_SHOP_ID = "shopId";
    public static final String CART_LIST = "cartList";
    
    /**
     * 订单相关字段名
     */
    public static final String ORDER_ID = "orderId";
    public static final String ORDER_USER_ID = "userId";
    public static final String ORDER_TOTAL_AMOUNT = "totalAmount";
    public static final String ORDER_STATUS = "status";
    public static final String ORDER_DELIVERY_DATE = "deliveryDate";
    public static final String ORDER_DELIVERY_TIME = "deliveryTime";
    public static final String ORDER_ADDRESS_ID = "addressId";
    public static final String ORDER_PAYMENT_METHOD = "paymentMethod";
    public static final String ORDER_CREATED_AT = "createdAt";
    public static final String ORDER_UPDATED_AT = "updatedAt";
    
    /**
     * 位置相关字段名
     */
    public static final String LOCATION_ID = "id";
    public static final String LOCATION_NAME = "name";
    public static final String LOCATION_ADDRESS = "address";
    public static final String LOCATION_LATITUDE = "latitude";
    public static final String LOCATION_LONGITUDE = "longitude";
    public static final String LOCATION_TYPE = "type";
    
    /**
     * 轮播图相关字段名
     */
    public static final String BANNER_ID = "id";
    public static final String BANNER_IMG_URL = "imgUrl";
    public static final String BANNER_TITLE = "title";
    public static final String BANNER_LINK_URL = "linkUrl";
    public static final String BANNER_TYPE = "type";
    public static final String BANNER_SORT_ORDER = "sortOrder";
    public static final String BANNER_ACTIVE = "active";
    
    /**
     * 响应消息常量
     */
    public static final String SUCCESS_MESSAGE = "操作成功";
    public static final String ERROR_MESSAGE = "操作失败";
    public static final String UNAUTHORIZED_MESSAGE = "用户认证失败";
    public static final String DATA_NOT_FOUND_MESSAGE = "数据未找到";
    
    /**
     * 用户相关消息
     */
    public static final String USER_NOT_FOUND_MESSAGE = "用户不存在";
    public static final String USER_PHONE_EMPTY_MESSAGE = "手机号不能为空";
    public static final String USER_PASSWORD_EMPTY_MESSAGE = "密码不能为空";
    public static final String USER_PHONE_EXISTS_MESSAGE = "手机号已存在，请使用其他手机号";
    public static final String USER_EMAIL_EXISTS_MESSAGE = "邮箱已存在，请使用其他邮箱";
    public static final String USER_PHONE_NOT_REGISTERED_MESSAGE = "手机号未注册";
    public static final String USER_PASSWORD_INCORRECT_MESSAGE = "密码错误";
    public static final String USER_REGISTER_SUCCESS_MESSAGE = "用户注册成功";
    public static final String USER_REGISTER_FAILED_MESSAGE = "用户注册失败，请重试";
    public static final String USER_LOGIN_SUCCESS_MESSAGE = "登录成功";
    public static final String USER_LOGIN_FAILED_MESSAGE = "登录服务暂时不可用，请重试";
    public static final String INVALID_TOKEN_MESSAGE = "无效的token";
    public static final String USER_PROFILE_SUCCESS_MESSAGE = "用户资料获取成功";
    public static final String USER_PROFILE_DEFAULT_MESSAGE = "用户资料获取成功（使用默认数据）";
    public static final String USER_PROFILE_FAILED_MESSAGE = "获取用户资料失败，请重试";
    
    /**
     * 商品相关消息
     */
    public static final String PRODUCT_NOT_FOUND_MESSAGE = "商品不存在";
    public static final String PRODUCT_DATA_NOT_FOUND_MESSAGE = "数据库中未找到商品数据";
    public static final String PRODUCT_LIST_SUCCESS_MESSAGE = "商品列表获取成功";
    public static final String PRODUCT_DETAIL_SUCCESS_MESSAGE = "商品详情获取成功";
    public static final String PRODUCT_DETAIL_FAILED_MESSAGE = "获取商品详情失败，请重试";
    public static final String CATEGORY_PRODUCTS_SUCCESS_MESSAGE = "分类商品列表获取成功";
    public static final String CATEGORY_PRODUCTS_FAILED_MESSAGE = "获取分类商品列表失败";
    
    /**
     * 分类相关消息
     */
    public static final String CATEGORY_LIST_SUCCESS_MESSAGE = "分类列表获取成功";
    public static final String CATEGORY_DATA_NOT_FOUND_MESSAGE = "数据库中未找到分类和标签数据";
    
    /**
     * 购物车相关消息
     */
    public static final String CART_ITEM_COUNT_SUCCESS_MESSAGE = "购物车商品数量获取成功";
    public static final String CART_ADD_SUCCESS_MESSAGE = "商品已成功添加到购物车";
    public static final String CART_ADD_FAILED_MESSAGE = "添加到购物车失败，请重试";
    public static final String CART_CHANGE_SUCCESS_MESSAGE = "购物车更新成功";
    public static final String CART_CHANGE_FAILED_MESSAGE = "购物车更新失败，请重试";
    public static final String CART_PRODUCTS_SUCCESS_MESSAGE = "购物车商品列表获取成功";
    public static final String CART_PRODUCTS_FAILED_MESSAGE = "获取购物车商品列表失败，请重试";
    public static final String CART_PRODUCT_INFO_SUCCESS_MESSAGE = "商品购物车信息获取成功";
    public static final String CART_PRODUCT_INFO_FAILED_MESSAGE = "获取商品购物车信息失败，请重试";
    
    /**
     * 订单相关消息
     */
    public static final String ORDER_SUBMIT_SUCCESS_MESSAGE = "订单提交成功";
    public static final String ORDER_SUBMIT_FAILED_MESSAGE = "订单提交失败，请重试";
    public static final String ORDER_DETAIL_SUCCESS_MESSAGE = "订单详情获取成功";
    public static final String ORDER_DETAIL_FAILED_MESSAGE = "获取订单详情失败，请重试";
    public static final String ORDER_PAYMENT_SUCCESS_MESSAGE = "支付处理成功";
    public static final String ORDER_PAYMENT_FAILED_MESSAGE = "支付处理失败";
    public static final String ORDER_PAYMENT_PROCESS_FAILED_MESSAGE = "支付处理失败，请重试";
    public static final String USER_ADDRESSES_FAILED_MESSAGE = "获取用户地址失败，请重试";
    
    /**
     * 搜索相关消息
     */
    public static final String HOT_SEARCH_SUCCESS_MESSAGE = "热门搜索列表获取成功";
    public static final String HOT_SEARCH_FAILED_MESSAGE = "获取热门搜索列表失败，请重试";
    public static final String PRODUCT_SEARCH_SUCCESS_MESSAGE = "商品搜索完成";
    public static final String PRODUCT_SEARCH_FAILED_MESSAGE = "商品搜索失败，请重试";
    
    /**
     * 主页相关消息
     */
    public static final String HOME_DATA_SUCCESS_MESSAGE = "主页数据获取成功";
    public static final String HOME_DATA_FAILED_MESSAGE = "主页数据获取失败";
    public static final String LOCATION_DATA_SUCCESS_MESSAGE = "位置信息获取成功";
    public static final String BANNER_DATA_SUCCESS_MESSAGE = "轮播图数据获取成功";
    
    /**
     * 用户地址相关字段名
     */
    public static final String ADDRESS_ID = "id";
    public static final String ADDRESS_NAME = "name";
    public static final String ADDRESS_PHONE = "phone";
    public static final String ADDRESS_ADDRESS = "address";
    public static final String ADDRESS_IS_DEFAULT = "isDefault";
    
    /**
     * 用户地址相关消息
     */
    public static final String USER_ADDRESSES_SUCCESS_MESSAGE = "用户地址列表获取成功";
    public static final String USER_ADDRESSES_NOT_FOUND_MESSAGE = "数据库中未找到用户地址数据";
    
    /**
     * 配送时间相关字段名
     */
    public static final String DELIVERY_DATE = "date";
    public static final String DELIVERY_TIME_SLOTS = "timeSlots";
    
    /**
     * 配送时间相关消息
     */
    public static final String DELIVERY_TIME_SUCCESS_MESSAGE = "配送时间获取成功";
    
    /**
     * 支付方式相关字段名
     */
    public static final String PAYMENT_METHOD_ID = "id";
    public static final String PAYMENT_METHOD_NAME = "name";
    public static final String PAYMENT_METHOD_ICON = "icon";
    public static final String PAYMENT_METHOD_IS_ACTIVE = "isActive";
    
    /**
     * 支付方式相关消息
     */
    public static final String PAYMENT_METHODS_SUCCESS_MESSAGE = "支付方式获取成功";
    

    
    /**
     * 默认值常量
     */
    public static final String DEFAULT_USER_ID = "12";
    public static final String DEFAULT_LOCATION_ID = "001";
    public static final String DEFAULT_LOCATION_NAME = "Ottawa(Chinatown store)";
    public static final String DEFAULT_AVATAR = "/images/external/category-list-5.png";
    public static final String DEFAULT_VIP_LEVEL = "VIP1";
    public static final String DEFAULT_GUEST_NICKNAME = "Guest User";
    public static final String DEFAULT_SHOPPER_NICKNAME = "Shopper";
    
    /**
     * 状态常量
     */
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_PAID = "PAID";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    
    /**
     * 缓存键名常量
     */
    public static final String CACHE_PRODUCTS = "products";
    public static final String CACHE_CATEGORIES = "categories";
    public static final String CACHE_USER_PROFILES = "userProfiles";
    public static final String CACHE_LOCATIONS = "locations";
    public static final String CACHE_BANNERS = "banners";
    public static final String CACHE_HOT_SEARCHES = "hotSearches";
    public static final String CACHE_NEARBY_STORES = "nearbyStores";
    
    /**
     * 数据库字段名常量
     */
    public static final String DB_CREATED_AT = "created_at";
    public static final String DB_UPDATED_AT = "updated_at";
    public static final String DB_IS_ACTIVE = "is_active";
    public static final String DB_SORT_ORDER = "sort_order";
    public static final String DB_USER_ID = "user_id";
    public static final String DB_PRODUCT_ID = "product_id";
    public static final String DB_ORDER_ID = "order_id";
    public static final String DB_LOCATION_ID = "location_id";
    public static final String DB_BANNER_ID = "banner_id";
    public static final String DB_SEARCH_ID = "search_id";
    public static final String DB_VIP_LEVEL = "vip_level";
    public static final String DB_REWARD_POINTS = "reward_points";
    public static final String DB_PHONE_NUMBER = "phone_number";
    public static final String DB_IMG_URL = "img_url";
    public static final String DB_LINK_URL = "link_url";
    public static final String DB_DELIVERY_DATE = "delivery_date";
    public static final String DB_DELIVERY_TIME = "delivery_time";
    public static final String DB_ADDRESS_ID = "address_id";
    public static final String DB_PAYMENT_METHOD = "payment_method";
    public static final String DB_TOTAL_AMOUNT = "total_amount";
    public static final String DB_SEARCH_COUNT = "search_count";
    
    /**
     * 附近商店相关字段名
     */
    public static final String STORE_ID = "id";
    public static final String STORE_NAME = "name";
    public static final String STORE_ADDRESS = "address";
    public static final String STORE_PHONE = "phone";
    public static final String STORE_LATITUDE = "latitude";
    public static final String STORE_LONGITUDE = "longitude";
    public static final String STORE_DISTANCE = "distance";
    
    /**
     * 附近商店相关消息
     */
    public static final String NEARBY_STORES_SUCCESS_MESSAGE = "附近商店列表获取成功";
    public static final String NEARBY_STORES_NOT_FOUND_MESSAGE = "数据库中未找到商店数据";
    public static final String NEARBY_STORES_FAILED_MESSAGE = "获取附近商店列表失败";


    public static final String TRANSLATIONS_TYPE_SYSTEM_MESSAGE = "system_message";

}
