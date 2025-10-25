-- 示例迁移：添加用户偏好设置表
-- V3__Add_user_preferences_table.sql
-- 展示如何使用Flyway添加新功能

-- 创建用户偏好设置表
CREATE TABLE IF NOT EXISTS user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    theme VARCHAR(20) DEFAULT 'light',
    notifications_enabled BOOLEAN DEFAULT TRUE,
    email_notifications BOOLEAN DEFAULT TRUE,
    sms_notifications BOOLEAN DEFAULT FALSE,
    currency VARCHAR(3) DEFAULT 'CAD',
    timezone VARCHAR(50) DEFAULT 'America/Toronto',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(user_id)
);

-- 添加表注释
COMMENT ON TABLE user_preferences IS '用户偏好设置表';
COMMENT ON COLUMN user_preferences.theme IS 'UI主题偏好: light(浅色), dark(深色)';
COMMENT ON COLUMN user_preferences.notifications_enabled IS '全局通知设置';
COMMENT ON COLUMN user_preferences.email_notifications IS '邮件通知设置';
COMMENT ON COLUMN user_preferences.sms_notifications IS '短信通知设置';
COMMENT ON COLUMN user_preferences.currency IS '货币偏好: CAD(加元), USD(美元), CNY(人民币)';
COMMENT ON COLUMN user_preferences.timezone IS '时区设置';

-- 为现有用户创建默认偏好设置
INSERT INTO user_preferences (user_id, theme, notifications_enabled, email_notifications, sms_notifications, currency, timezone)
SELECT 
    id,
    'light',
    true,
    true,
    false,
    'CAD',
    'America/Toronto'
FROM users 
WHERE NOT EXISTS (
    SELECT 1 FROM user_preferences WHERE user_preferences.user_id = users.id
);
