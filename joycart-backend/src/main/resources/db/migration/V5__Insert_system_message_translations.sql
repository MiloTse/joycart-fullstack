-- JoyCart 系统消息翻译数据补丁
-- V5__Insert_system_message_translations.sql
-- 由于V4迁移执行时表已存在，但没有数据，这里补插系统消息数据

-- 插入系统消息翻译数据
INSERT INTO translations (entity_type, entity_id, field_name, language_code, translation_text) VALUES

-- 登录相关消息
('system_message', 0, 'login.success', 'en-US', 'Login successfully'),
('system_message', 0, 'login.success', 'zh-CN', '登录成功'),
('system_message', 0, 'login.success', 'fr-FR', 'Connexion réussie'),

-- 注册相关消息
('system_message', 0, 'register.success', 'en-US', 'Register successfully'),
('system_message', 0, 'register.success', 'zh-CN', '用户注册成功'),
('system_message', 0, 'register.success', 'fr-FR', 'Inscription réussie')

ON CONFLICT (entity_type, entity_id, field_name, language_code) DO NOTHING;

