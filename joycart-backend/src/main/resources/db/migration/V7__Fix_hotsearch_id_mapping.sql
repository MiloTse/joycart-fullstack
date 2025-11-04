-- JoyCart 修复热门搜索ID映射
-- V7__Fix_hotsearch_id_mapping.sql
-- 确保hot_searches表的id与翻译表的entity_id正确对应

-- 如果hot_searches表已经存在数据，需要更新id以匹配翻译表
-- 由于id是主键且可能已被引用，我们需要先删除翻译数据，然后重新插入hot_searches数据，最后重新插入翻译数据

-- 步骤1: 删除现有的热门搜索翻译数据（如果存在）
DELETE FROM translations WHERE entity_type = 'hot_search';

-- 步骤2: 删除现有的热门搜索数据
DELETE FROM hot_searches;

-- 步骤3: 重新插入热门搜索数据，明确指定id为1-8
INSERT INTO hot_searches (id, search_id, keyword, is_active, sort_order, search_count, created_at, updated_at) VALUES
(1, 'HS001', 'Fresh Fruit', true, 1, 1250, NOW(), NOW()),
(2, 'HS002', 'Organic Vegetables', true, 2, 980, NOW(), NOW()),
(3, 'HS003', 'Meat & Seafood', true, 3, 856, NOW(), NOW()),
(4, 'HS004', 'Dairy Products', true, 4, 742, NOW(), NOW()),
(5, 'HS005', 'Bakery Items', true, 5, 623, NOW(), NOW()),
(6, 'HS006', 'Frozen Foods', true, 6, 589, NOW(), NOW()),
(7, 'HS007', 'Beverages', true, 7, 456, NOW(), NOW()),
(8, 'HS008', 'Snacks', true, 8, 398, NOW(), NOW())
ON CONFLICT (id) DO UPDATE SET
    search_id = EXCLUDED.search_id,
    keyword = EXCLUDED.keyword,
    is_active = EXCLUDED.is_active,
    sort_order = EXCLUDED.sort_order,
    search_count = EXCLUDED.search_count,
    updated_at = NOW();

-- 步骤4: 重新插入热门搜索翻译数据（与V6迁移脚本中的翻译数据一致）
INSERT INTO translations (entity_type, entity_id, field_name, language_code, translation_text) VALUES
-- HotSearch ID 1: Fresh Fruit
('hot_search', 1, 'keyword', 'en-US', 'Fresh Fruit'),
('hot_search', 1, 'keyword', 'zh-CN', '新鲜水果'),
('hot_search', 1, 'keyword', 'fr-FR', 'Fruits frais'),

-- HotSearch ID 2: Organic Vegetables
('hot_search', 2, 'keyword', 'en-US', 'Organic Vegetables'),
('hot_search', 2, 'keyword', 'zh-CN', '有机蔬菜'),
('hot_search', 2, 'keyword', 'fr-FR', 'Légumes biologiques'),

-- HotSearch ID 3: Meat & Seafood
('hot_search', 3, 'keyword', 'en-US', 'Meat & Seafood'),
('hot_search', 3, 'keyword', 'zh-CN', '肉类海鲜'),
('hot_search', 3, 'keyword', 'fr-FR', 'Viande et fruits de mer'),

-- HotSearch ID 4: Dairy Products
('hot_search', 4, 'keyword', 'en-US', 'Dairy Products'),
('hot_search', 4, 'keyword', 'zh-CN', '乳制品'),
('hot_search', 4, 'keyword', 'fr-FR', 'Produits laitiers'),

-- HotSearch ID 5: Bakery Items
('hot_search', 5, 'keyword', 'en-US', 'Bakery Items'),
('hot_search', 5, 'keyword', 'zh-CN', '烘焙食品'),
('hot_search', 5, 'keyword', 'fr-FR', 'Articles de boulangerie'),

-- HotSearch ID 6: Frozen Foods
('hot_search', 6, 'keyword', 'en-US', 'Frozen Foods'),
('hot_search', 6, 'keyword', 'zh-CN', '冷冻食品'),
('hot_search', 6, 'keyword', 'fr-FR', 'Aliments surgelés'),

-- HotSearch ID 7: Beverages
('hot_search', 7, 'keyword', 'en-US', 'Beverages'),
('hot_search', 7, 'keyword', 'zh-CN', '饮料'),
('hot_search', 7, 'keyword', 'fr-FR', 'Boissons'),

-- HotSearch ID 8: Snacks
('hot_search', 8, 'keyword', 'en-US', 'Snacks'),
('hot_search', 8, 'keyword', 'zh-CN', '零食'),
('hot_search', 8, 'keyword', 'fr-FR', 'Collations')

ON CONFLICT (entity_type, entity_id, field_name, language_code) DO UPDATE SET
    translation_text = EXCLUDED.translation_text,
    updated_at = NOW();

