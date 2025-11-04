-- JoyCart 项目铺底数据迁移脚本
-- V2__Initial_data.sql
-- 基于 data.sql 文件插入初始数据

-- ===========================================
-- 1. 插入分类数据（基于原始Home页面的8个分类，2行4列布局）
-- ===========================================
INSERT INTO categories (id, name, img_url, description, is_active, sort_order, created_at, updated_at) VALUES
(8210, 'Produce', '/images/external/category-1.png', 'Fresh vegetables and greens', true, 1, NOW(), NOW()),
(8211, 'Meat & Fish', '/images/external/category-2.png', 'Fresh meat and seafood products', true, 2, NOW(), NOW()),
(8212, 'Fresh Fruit', '/images/external/category-3.png', 'Fresh fruits and citrus', true, 3, NOW(), NOW()),
(8213, 'Milk & Dairy', '/images/external/category-4.png', 'Dairy products and milk', true, 4, NOW(), NOW()),
(8214, 'Oils & Masala', '/images/external/category-5.png', 'Cooking oils and spices', true, 5, NOW(), NOW()),
(8215, 'Snack', '/images/external/category-6.png', 'Snacks and packaged foods', true, 6, NOW(), NOW()),
(8216, 'Appliances', '/images/external/category-7.png', 'Home appliances and furniture', true, 7, NOW(), NOW()),
(8217, 'Cosmetics', '/images/external/category-8.png', 'Beauty and personal care', true, 8, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ===========================================
-- 2. 插入标签数据（英文标签）
-- ===========================================
INSERT INTO tags (id, name, is_active, sort_order, created_at, updated_at) VALUES
(1, 'Fruits & Vegetables', true, 1, NOW(), NOW()),
(2, 'Meat & Poultry', true, 2, NOW(), NOW()),
(3, 'Seafood', true, 3, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ===========================================
-- 3. 插入商品数据（基于真实商品信息）
-- ===========================================
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
(10, '88395', 'Fresh Spinach Organic Leafy Greens 200g', 'Nutritious organic spinach, packed with vitamins and minerals', '/images/external/category-list-4.png', 12.9, 298, 'Organic Certified', '200g', 'Fresh organic spinach leaves, rich in iron and vitamins. Perfect for salads, smoothies, or cooking as a healthy side dish.', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ===========================================
-- 4. 插入附近商店数据
-- ===========================================
INSERT INTO nearby_stores (store_id, name, phone, address, latitude, longitude, distance, sort_order) VALUES
('8318', 'College Square Store', '1-613-001-9999', '1385 Woodroffe Ave, Ottawa, ON K4S 1A6, Canada', 45.3498, -75.7553, '799m', 1),
('8317', 'Kanata Technology Park Store', '1-613-003-8888', '350 Legget Dr, Kanata, ON K2K 3N1, Canada', 45.333, -75.7369, '1.1km', 2),
('8319', 'Rideau Centre Store', '1-613-005-7777', '50 Rideau St, Ottawa, ON K1N 9J7, Canada', 45.4215, -75.6919, '2.3km', 3),
('8320', 'Bayshore Shopping Centre Store', '1-613-007-6666', '100 Bayshore Dr, Ottawa, ON K2B 8C1, Canada', 45.3636, -75.8064, '3.8km', 4),
('8321', 'St. Laurent Shopping Centre Store', '1-613-009-5555', '1200 St Laurent Blvd, Ottawa, ON K1K 3B8, Canada', 45.4213, -75.6187, '4.2km', 5)
ON CONFLICT (store_id) DO NOTHING;

-- ===========================================
-- 5. 插入商店数据
-- ===========================================
INSERT INTO shops (shop_id, shop_name) VALUES
('8137', 'Mei''s Fresh Produce')
ON CONFLICT (shop_id) DO NOTHING;

-- ===========================================
-- 6. 插入位置数据
-- ===========================================
INSERT INTO locations (location_id, name, is_active, sort_order, created_at, updated_at) VALUES
('001', 'Ottawa(Chinatown store)', true, 1, NOW(), NOW()),
('002', 'Toronto(Downtown store)', true, 2, NOW(), NOW()),
('003', 'Vancouver(West End store)', true, 3, NOW(), NOW())
ON CONFLICT (location_id) DO NOTHING;

-- ===========================================
-- 7. 插入轮播图数据
-- ===========================================
INSERT INTO banners (banner_id, img_url, title, is_active, sort_order, created_at, updated_at) VALUES
('1135', '/images/banner01.png', 'Fresh Produce Banner', true, 1, NOW(), NOW()),
('1136', '/images/external/banner.png', 'Special Offer Banner', true, 2, NOW(), NOW()),
('1137', '/images/external/banner.png', 'New Arrivals Banner', true, 3, NOW(), NOW())
ON CONFLICT (banner_id) DO NOTHING;

-- ===========================================
-- 8. 插入热门搜索数据
-- 注意：id字段需要明确指定，以便与翻译表V6中的entity_id对应
-- ===========================================
INSERT INTO hot_searches (id, search_id, keyword, is_active, sort_order, search_count, created_at, updated_at) VALUES
(1, 'HS001', 'Fresh Fruit', true, 1, 1250, NOW(), NOW()),
(2, 'HS002', 'Organic Vegetables', true, 2, 980, NOW(), NOW()),
(3, 'HS003', 'Meat & Seafood', true, 3, 856, NOW(), NOW()),
(4, 'HS004', 'Dairy Products', true, 4, 742, NOW(), NOW()),
(5, 'HS005', 'Bakery Items', true, 5, 623, NOW(), NOW()),
(6, 'HS006', 'Frozen Foods', true, 6, 589, NOW(), NOW()),
(7, 'HS007', 'Beverages', true, 7, 456, NOW(), NOW()),
(8, 'HS008', 'Snacks', true, 8, 398, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ===========================================
-- 9. 插入用户资料数据
-- ===========================================
INSERT INTO user_profiles (user_id, nickname, avatar, vip_level, coupons, reward_points, phone_number, email, is_active, created_at, updated_at) VALUES
('12', 'Tom Wang', '/images/external/category-list-5.png', 'VIP5', 4, 258, '1234567890', 'tom.wang@example.com', true, NOW(), NOW()),
('13', 'Jerry Wang', '/images/external/category-list-1.png', 'VIP3', 2, 156, '0987654321', 'jerry.wang@example.com', true, NOW(), NOW()),
('14', 'Alice Smith', '/images/external/category-list-2.png', 'VIP1', 1, 89, '5551234567', 'alice.smith@example.com', true, NOW(), NOW())
ON CONFLICT (user_id) DO NOTHING;
