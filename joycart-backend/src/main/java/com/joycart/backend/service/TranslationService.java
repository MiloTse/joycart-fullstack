package com.joycart.backend.service;

import java.util.Map;

/**
 * 翻译服务接口 - 提供多语言翻译功能
 */
public interface TranslationService {
    
    /**
     * 获取指定实体的翻译文本
     * @param entityType 实体类型 (如: product, category, hot_search)
     * @param entityId 实体ID
     * @param fieldName 字段名 (如: title, subtitle, name, description)
     * @param languageCode 语言代码 (如: en-US, zh-CN, fr-FR)
     * @return 翻译文本，如果不存在则返回null
     */
    String getTranslation(String entityType, Long entityId, String fieldName, String languageCode);
    
    /**
     * 获取指定实体的所有翻译
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param languageCode 语言代码
     * @return Map<String, String> 字段名 -> 翻译文本
     */
    Map<String, String> getAllTranslations(String entityType, Long entityId, String languageCode);
    
    /**
     * 获取默认语言的翻译（通常是en-US）
     * 如果指定语言没有翻译，则返回默认语言的翻译
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param fieldName 字段名
     * @param languageCode 语言代码
     * @return 翻译文本
     */
    String getTranslationWithFallback(String entityType, Long entityId, String fieldName, String languageCode);
}

