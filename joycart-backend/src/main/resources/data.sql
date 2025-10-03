-- JoyCart 项目初始数据
-- 项目铺底数据。
-- 使用INSERT ... ON CONFLICT DO NOTHING确保可重复执行

-- 插入分类数据（恢复原始categoryAndTagList.json数据）
INSERT INTO categories (id, name, img_url, description, is_active, sort_order, created_at, updated_at) VALUES
(8210, '精选商品', '/images/external/category-1.png', '精选商品类', true, 1, NOW(), NOW()),
(8211, '单品优惠', '/images/external/category-2.png', '单品优惠类', true, 2, NOW(), NOW()),
(8212, '新鲜水果', '/images/external/category-3.png', '新鲜水果类', true, 3, NOW(), NOW()),
(8213, '时令蔬菜', '/images/external/category-4.png', '时令蔬菜类', true, 4, NOW(), NOW()),
(8214, '肉蛋家禽', '/images/external/category-5.png', '肉蛋家禽类', true, 5, NOW(), NOW()),
(8215, '水产海鲜', '/images/external/category-6.png', '水产海鲜类', true, 6, NOW(), NOW()),
(8216, '牛奶面包', '/images/external/category-7.png', '牛奶面包类', true, 7, NOW(), NOW()),
(8217, '冷冻冷藏', '/images/external/category-8.png', '冷冻冷藏类', true, 8, NOW(), NOW()),
(8218, '米面粮油', '/images/external/category-1.png', '米面粮油类', true, 9, NOW(), NOW()),
(8219, '生活用品', '/images/external/category-2.png', '生活用品类', true, 10, NOW(), NOW()),
(8220, '进口食品', '/images/external/category-3.png', '进口食品类', true, 11, NOW(), NOW()),
(8221, '精美礼盒', '/images/external/category-4.png', '精美礼盒类', true, 12, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- 插入标签数据（恢复原始categoryAndTagList.json数据）
INSERT INTO tags (id, name, is_active, sort_order, created_at, updated_at) VALUES
(1, '果蔬', true, 1, NOW(), NOW()),
(2, '肉蛋家禽', true, 2, NOW(), NOW()),
(3, '海鲜', true, 3, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- 插入商品数据（基于真实商品信息）
INSERT INTO products (id, product_id, title, subtitle, img_url, price, sales, origin, specification, detail, is_active, created_at, updated_at) VALUES
(1, '1132381', 'Domestic pork, skinless pork belly blocks', 'Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce', '/images/external/fresh-1.png', 66.9, 156, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(2, '1132382', 'Prime live Boston lobster 2 pcs large package', 'Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce', '/images/external/fresh-2.png', 98.0, 89, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(3, '1132383', 'Prime imported salmon 2 pcs large package', 'Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce', '/images/external/fresh-3.png', 378.0, 45, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(4, '1132384', 'Fresh frozen squid head frozen squid tentacles 400g', 'Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce', '/images/external/fresh-4.png', 39.9, 203, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(5, '1132385', 'chicken wing middle 1000g/...', 'Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce', '/images/external/fresh-1.png', 156.0, 156, 'Based on actual purchased product batch', '2kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(6, '88391', 'Shandong Haiyang Provence Tomatoes Natural Ripe Sandy Tomatoes Fresh Fruit Vegetables Healthy Food Premium Box 5kg', 'Fresh, crisp, and refreshing taste with natural white fuzz characteristic of this seasonal produce', '/images/external/detail.png', 39.9, 456, 'Based on actual purchased product batch', '5kg', 'Provence tomatoes differ from other tomato varieties with their ribbed and grooved surface, unlike smooth high-powder tomatoes. They resemble autumn persimmons or bell peppers. Due to their grooved structure, they have a hollow interior with a strawberry-like heart pattern, most noticeable in the first two layers, improving in the third and fourth layers, and rarely present in the fifth layer.', true, NOW(), NOW()),
(7, '88392', 'Organic Fresh Strawberries Premium Quality Sweet Red Berries 500g', 'Sweet and juicy organic strawberries, perfect for desserts and snacks', '/images/external/category-list-1.png', 24.9, 234, 'Organic Farm Certified', '500g', 'Premium quality organic strawberries, carefully selected for their sweetness and freshness. Perfect for making desserts, smoothies, or enjoying as a healthy snack.', true, NOW(), NOW()),
(8, '88393', 'Fresh Green Zucchini Organic Vegetables 1kg', 'Crisp and fresh organic zucchini, ideal for healthy cooking', '/images/external/category-list-2.png', 18.9, 189, 'Local Organic Farm', '1kg', 'Fresh organic zucchini with excellent texture and flavor. Perfect for grilling, roasting, or adding to salads and stir-fries.', true, NOW(), NOW()),
(9, '88394', 'Premium Avocado Hass Variety 4 pieces', 'Creamy and nutritious Hass avocados, perfect for healthy meals', '/images/external/category-list-3.png', 32.9, 167, 'California Grown', '4 pieces', 'Premium Hass avocados with creamy texture and rich flavor. Perfect for making guacamole, adding to salads, or enjoying on toast.', true, NOW(), NOW()),
(10, '88395', 'Fresh Spinach Organic Leafy Greens 200g', 'Nutritious organic spinach, packed with vitamins and minerals', '/images/external/category-list-4.png', 12.9, 298, 'Organic Certified', '200g', 'Fresh organic spinach leaves, rich in iron and vitamins. Perfect for salads, smoothies, or cooking as a healthy side dish.', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;


CREATE TABLE nearby_stores (
                               id BIGSERIAL PRIMARY KEY,
                               store_id VARCHAR(50) UNIQUE NOT NULL,  -- 商店唯一标识
                               name VARCHAR(200) NOT NULL,            -- 商店名称
                               phone VARCHAR(50),                      -- 联系电话
                               address TEXT NOT NULL,                  -- 详细地址
                               latitude DECIMAL(10, 7),               -- 纬度
                               longitude DECIMAL(10, 7),              -- 经度
                               distance VARCHAR(20),                   -- 距离描述（如"799m"）
                               is_active BOOLEAN DEFAULT TRUE,        -- 是否激活
                               sort_order INTEGER DEFAULT 0,          -- 排序字段
                               created_at TIMESTAMP DEFAULT NOW(),    -- 创建时间
                               updated_at TIMESTAMP DEFAULT NOW()     -- 更新时间
);

-- 插入附近商店数据, 铺底数据。
INSERT INTO nearby_stores (store_id, name, phone, address, latitude, longitude, distance, sort_order) VALUES
                                                                                                          ('8318', 'College Square Store', '1-613-001-9999', '1385 Woodroffe Ave, Ottawa, ON K4S 1A6, Canada', 45.3498, -75.7553, '799m', 1),
                                                                                                          ('8317', 'Kanata Technology Park Store', '1-613-003-8888', '350 Legget Dr, Kanata, ON K2K 3N1, Canada', 45.333, -75.7369, '1.1km', 2),
                                                                                                          ('8319', 'Rideau Centre Store', '1-613-005-7777', '50 Rideau St, Ottawa, ON K1N 9J7, Canada', 45.4215, -75.6919, '2.3km', 3),
                                                                                                          ('8320', 'Bayshore Shopping Centre Store', '1-613-007-6666', '100 Bayshore Dr, Ottawa, ON K2B 8C1, Canada', 45.3636, -75.8064, '3.8km', 4),
                                                                                                          ('8321', 'St. Laurent Shopping Centre Store', '1-613-009-5555', '1200 St Laurent Blvd, Ottawa, ON K1K 3B8, Canada', 45.4213, -75.6187, '4.2km', 5)
ON CONFLICT (store_id) DO NOTHING;



CREATE TABLE user_addresses (
                                id BIGSERIAL PRIMARY KEY,
                                user_id INTEGER NOT NULL,              -- 用户ID，关联users表
                                name VARCHAR(100) NOT NULL,            -- 收货人姓名
                                phone VARCHAR(50) NOT NULL,            -- 联系电话
                                address TEXT NOT NULL,                 -- 详细地址
                                is_default BOOLEAN DEFAULT FALSE,      -- 是否为默认地址
                                is_active BOOLEAN DEFAULT TRUE,        -- 是否激活
                                created_at TIMESTAMP DEFAULT NOW(),    -- 创建时间
                                updated_at TIMESTAMP DEFAULT NOW()     -- 更新时间
);
-- 插入用户地址数据
INSERT INTO user_addresses (user_id, name, phone, address, is_default) VALUES
                                                                           (1, 'John Zhang', '13800138000', 'Room 101, Unit 1, Building 1, Residential Complex, Chaoyang District, Beijing', true),
                                                                           (1, 'Mike Li', '13900139000', 'Room 2001, 20th Floor, Office Building, Pudong New Area, Shanghai', false),
                                                                           (1, 'Jerry Wang', '1-613-727-4723', '1385 Woodroffe Avenue, Ottawa, ON, K2G 1V8', false)
ON CONFLICT (id) DO NOTHING;




























