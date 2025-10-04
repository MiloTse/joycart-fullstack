-- JoyCart 项目初始数据
-- 项目铺底数据。
-- 此脚本可重复执行，每次执行会先清空现有数据，然后重新插入铺底数据

-- ===========================================
-- 1. 创建表结构（如果不存在）
-- ===========================================

-- 创建分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    img_url VARCHAR(500),
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建标签表
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建商品表
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY,
    product_id VARCHAR(50) UNIQUE NOT NULL,
    title VARCHAR(500) NOT NULL,
    subtitle TEXT,
    img_url VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    sales INTEGER DEFAULT 0,
    origin VARCHAR(200),
    specification VARCHAR(200),
    detail TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建附近商店表
CREATE TABLE IF NOT EXISTS nearby_stores (
    id BIGSERIAL PRIMARY KEY,
    store_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    phone VARCHAR(50),
    address TEXT NOT NULL,
    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),
    distance VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建用户地址表
CREATE TABLE IF NOT EXISTS user_addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    address TEXT NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(50) UNIQUE NOT NULL,
    user_id INTEGER NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    delivery_date DATE,
    delivery_time VARCHAR(10),
    address_id BIGINT,
    payment_method VARCHAR(50),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建订单项表
CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建购物车表
CREATE TABLE IF NOT EXISTS carts (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    count INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建商店数据表
CREATE TABLE IF NOT EXISTS shops (
    id BIGSERIAL PRIMARY KEY,
    shop_id VARCHAR(50) UNIQUE NOT NULL,
    shop_name VARCHAR(200) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建位置表
CREATE TABLE IF NOT EXISTS locations (
    id BIGSERIAL PRIMARY KEY,
    location_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建轮播图表
CREATE TABLE IF NOT EXISTS banners (
    id BIGSERIAL PRIMARY KEY,
    banner_id VARCHAR(50) UNIQUE NOT NULL,
    img_url VARCHAR(500) NOT NULL,
    title VARCHAR(200),
    link_url VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- 创建热门搜索表
CREATE TABLE IF NOT EXISTS hot_searches (
    id BIGSERIAL PRIMARY KEY,
    search_id VARCHAR(50) UNIQUE NOT NULL,
    keyword VARCHAR(200) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    search_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- ===========================================
-- 2. 删除现有数据（按依赖关系顺序删除）
-- ===========================================

-- 删除现有数据（按依赖关系顺序删除，避免外键约束错误）
-- 先删除子表，再删除父表
DELETE FROM order_items;        -- 订单项表（依赖orders表）
DELETE FROM orders;             -- 订单表（依赖users表）
DELETE FROM carts;              -- 购物车表（依赖users和products表）
DELETE FROM user_addresses;     -- 用户地址表（依赖users表）
DELETE FROM nearby_stores;      -- 附近商店表（独立表）
DELETE FROM products;           -- 商品表（依赖categories表）
DELETE FROM tags;               -- 标签表（独立表）
DELETE FROM categories;         -- 分类表（独立表）
DELETE FROM shops;              -- 商店表（独立表）
DELETE FROM banners;            -- 轮播图表（独立表）
DELETE FROM locations;          -- 位置表（独立表）
DELETE FROM hot_searches;       -- 热门搜索表（独立表）
-- 注意：users表通常不删除，因为包含用户账户信息

-- ===========================================
-- 3. 插入铺底数据
-- ===========================================

-- 插入分类数据（基于原始Home页面的8个分类，2行4列布局）
INSERT INTO categories (id, name, img_url, description, is_active, sort_order, created_at, updated_at) VALUES
(8210, 'Produce', '/images/external/category-1.png', 'Fresh vegetables and greens', true, 1, NOW(), NOW()),
(8211, 'Meat & Fish', '/images/external/category-2.png', 'Fresh meat and seafood products', true, 2, NOW(), NOW()),
(8212, 'Fresh Fruit', '/images/external/category-3.png', 'Fresh fruits and citrus', true, 3, NOW(), NOW()),
(8213, 'Milk & Dairy', '/images/external/category-4.png', 'Dairy products and milk', true, 4, NOW(), NOW()),
(8214, 'Oils & Masala', '/images/external/category-5.png', 'Cooking oils and spices', true, 5, NOW(), NOW()),
(8215, 'Snack', '/images/external/category-6.png', 'Snacks and packaged foods', true, 6, NOW(), NOW()),
(8216, 'Appliances', '/images/external/category-7.png', 'Home appliances and furniture', true, 7, NOW(), NOW()),
(8217, 'Cosmetics', '/images/external/category-8.png', 'Beauty and personal care', true, 8, NOW(), NOW());

-- 插入标签数据（英文标签）
INSERT INTO tags (id, name, is_active, sort_order, created_at, updated_at) VALUES
(1, 'Fruits & Vegetables', true, 1, NOW(), NOW()),
(2, 'Meat & Poultry', true, 2, NOW(), NOW()),
(3, 'Seafood', true, 3, NOW(), NOW());

-- 插入商品数据（基于真实商品信息）
INSERT INTO products (id, product_id, title, subtitle, img_url, price, sales, origin, specification, detail, is_active, created_at, updated_at) VALUES
(1, '1132381', 'Domestic pork, skinless pork belly blocks', 'Domestic pork, skinless pork belly blocks', '/images/external/fresh-1.png', 66.9, 156, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(2, '1132382', 'Prime live Boston lobster 2 pcs large package', 'Prime live Boston lobster 2 pcs large package', '/images/external/fresh-2.png', 98.0, 89, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(3, '1132383', 'Prime imported salmon 2 pcs large package', 'Prime imported salmon 2 pcs large package', '/images/external/fresh-3.png', 378.0, 45, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(4, '1132384', 'Fresh frozen squid head frozen squid tentacles 400g', 'Fresh frozen squid head frozen squid tentacles 400g', '/images/external/fresh-4.png', 39.9, 203, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(5, '1132385', 'chicken wing middle 1000g/...', 'chicken wing middle 1000g/...', '/images/external/fresh-1.png', 156.0, 156, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(6, '88391', 'Shandong Haiyang Provence Tomatoes Natural Ripe Sandy Tomatoes Fresh Fruit Vegetables Healthy Food Premium Box 5kg', 'Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce', '/images/external/detail.png', 39.9, 456, 'Based on actual purchased product batch', '5kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(7, '88392', 'Organic Fresh Strawberries Premium Quality Sweet Red Berries 500g', 'Sweet and juicy organic strawberries, perfect for desserts and snacks', '/images/external/category-list-1.png', 24.9, 234, 'Organic Farm Certified', '500g', 'Premium quality organic strawberries, carefully selected for their sweetness and freshness. Perfect for making desserts, smoothies, or enjoying as a healthy snack.', true, NOW(), NOW()),
(8, '88393', 'Fresh Green Zucchini Organic Vegetables 1kg', 'Crisp and fresh organic zucchini, ideal for healthy cooking', '/images/external/category-list-2.png', 18.9, 189, 'Local Organic Farm', '1kg', 'Fresh organic zucchini with excellent texture and flavor. Perfect for grilling, roasting, or adding to salads and stir-fries.', true, NOW(), NOW()),
(9, '88394', 'Premium Avocado Hass Variety 4 pieces', 'Creamy and nutritious Hass avocados, perfect for healthy meals', '/images/external/category-list-3.png', 32.9, 167, 'California Grown', '4 pieces', 'Premium Hass avocados with creamy texture and rich flavor. Perfect for making guacamole, adding to salads, or enjoying on toast.', true, NOW(), NOW()),
(10, '88395', 'Fresh Spinach Organic Leafy Greens 200g', 'Nutritious organic spinach, packed with vitamins and minerals', '/images/external/category-list-4.png', 12.9, 298, 'Organic Certified', '200g', 'Fresh organic spinach leaves, rich in iron and vitamins. Perfect for salads, smoothies, or cooking as a healthy side dish.', true, NOW(), NOW());

-- 插入附近商店数据
INSERT INTO nearby_stores (store_id, name, phone, address, latitude, longitude, distance, sort_order) VALUES
('8318', 'College Square Store', '1-613-001-9999', '1385 Woodroffe Ave, Ottawa, ON K4S 1A6, Canada', 45.3498, -75.7553, '799m', 1),
('8317', 'Kanata Technology Park Store', '1-613-003-8888', '350 Legget Dr, Kanata, ON K2K 3N1, Canada', 45.333, -75.7369, '1.1km', 2),
('8319', 'Rideau Centre Store', '1-613-005-7777', '50 Rideau St, Ottawa, ON K1N 9J7, Canada', 45.4215, -75.6919, '2.3km', 3),
('8320', 'Bayshore Shopping Centre Store', '1-613-007-6666', '100 Bayshore Dr, Ottawa, ON K2B 8C1, Canada', 45.3636, -75.8064, '3.8km', 4),
('8321', 'St. Laurent Shopping Centre Store', '1-613-009-5555', '1200 St Laurent Blvd, Ottawa, ON K1K 3B8, Canada', 45.4213, -75.6187, '4.2km', 5);

-- 插入用户地址数据
INSERT INTO user_addresses (user_id, name, phone, address, is_default) VALUES
(1, 'John Zhang', '13800138000', 'Room 101, Unit 1, Building 1, Residential Complex, Chaoyang District, Beijing', true),
(1, 'Mike Li', '13900139000', 'Room 2001, 20th Floor, Office Building, Pudong New Area, Shanghai', false),
(1, 'Jerry Wang', '1-613-727-4723', '1385 Woodroffe Avenue, Ottawa, ON, K2G 1V8', false);

-- 插入商店数据
INSERT INTO shops (shop_id, shop_name) VALUES
('8137', 'Mei''s Fresh Produce');

-- 插入位置数据
INSERT INTO locations (location_id, name, is_active, sort_order, created_at, updated_at) VALUES
('001', 'Ottawa(Chinatown store)', true, 1, NOW(), NOW()),
('002', 'Toronto(Downtown store)', true, 2, NOW(), NOW()),
('003', 'Vancouver(West End store)', true, 3, NOW(), NOW());

-- 插入轮播图数据
INSERT INTO banners (banner_id, img_url, title, is_active, sort_order, created_at, updated_at) VALUES
('1135', '/images/banner01.png', 'Fresh Produce Banner', true, 1, NOW(), NOW()),
('1136', '/images/external/banner.png', 'Special Offer Banner', true, 2, NOW(), NOW()),
('1137', '/images/external/banner.png', 'New Arrivals Banner', true, 3, NOW(), NOW());

-- 插入热门搜索数据
INSERT INTO hot_searches (search_id, keyword, is_active, sort_order, search_count, created_at, updated_at) VALUES
('HS001', 'Fresh Fruit', true, 1, 1250, NOW(), NOW()),
('HS002', 'Organic Vegetables', true, 2, 980, NOW(), NOW()),
('HS003', 'Meat & Seafood', true, 3, 856, NOW(), NOW()),
('HS004', 'Dairy Products', true, 4, 742, NOW(), NOW()),
('HS005', 'Bakery Items', true, 5, 623, NOW(), NOW()),
('HS006', 'Frozen Foods', true, 6, 589, NOW(), NOW()),
('HS007', 'Beverages', true, 7, 456, NOW(), NOW()),
('HS008', 'Snacks', true, 8, 398, NOW(), NOW());