-- JoyCart 翻译表创建脚本
-- V4__Create_translations_table.sql
-- 语言: 英语(en-US)默认, 中文(zh-CN), 法语(fr-FR)

-- 创建translations翻译表
CREATE TABLE IF NOT EXISTS translations (
    id BIGSERIAL PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    field_name VARCHAR(50) NOT NULL,
    language_code VARCHAR(10) NOT NULL,
    translation_text TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(entity_type, entity_id, field_name, language_code)
);

-- 添加索引
CREATE INDEX IF NOT EXISTS idx_translations_entity_lang ON translations(entity_type, entity_id, language_code);
CREATE INDEX IF NOT EXISTS idx_translations_language ON translations(language_code);

-- 添加注释
COMMENT ON TABLE translations IS '多语言翻译表';
COMMENT ON COLUMN translations.entity_type IS '实体类型';
COMMENT ON COLUMN translations.entity_id IS '实体ID';
COMMENT ON COLUMN translations.field_name IS '字段名';
COMMENT ON COLUMN translations.language_code IS '语言代码: en-US, zh-CN, fr-FR';
COMMENT ON COLUMN translations.translation_text IS '翻译内容';

