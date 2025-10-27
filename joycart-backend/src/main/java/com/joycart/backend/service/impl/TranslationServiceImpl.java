package com.joycart.backend.service.impl;

import com.joycart.backend.model.Translation;
import com.joycart.backend.repository.TranslationRepository;
import com.joycart.backend.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻译服务实现类
 */
@Service
public class TranslationServiceImpl implements TranslationService {

    private static final Logger logger = LoggerFactory.getLogger(TranslationServiceImpl.class);

    @Autowired
    private TranslationRepository translationRepository;

    @Override
    public String getTranslation(String entityType, Long entityId, String fieldName, String languageCode) {
        logger.debug("Getting translation for entityType={}, entityId={}, fieldName={}, languageCode={}", 
            entityType, entityId, fieldName, languageCode);
        
        try {
            List<Translation> translations = translationRepository.findByEntityTypeAndEntityIdAndLanguageCode(
                entityType, entityId, languageCode
            );
            
            return translations.stream()
                .filter(t -> fieldName.equals(t.getFieldName()))
                .map(Translation::getTranslationText)
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            logger.error("Error getting translation: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Map<String, String> getAllTranslations(String entityType, Long entityId, String languageCode) {
        logger.debug("Getting all translations for entityType={}, entityId={}, languageCode={}", 
            entityType, entityId, languageCode);
        
        try {
            List<Translation> translations = translationRepository.findByEntityTypeAndEntityIdAndLanguageCode(
                entityType, entityId, languageCode
            );
            
            Map<String, String> result = new HashMap<>();
            for (Translation translation : translations) {
                result.put(translation.getFieldName(), translation.getTranslationText());
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Error getting all translations: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    @Override
    public String getTranslationWithFallback(String entityType, Long entityId, String fieldName, String languageCode) {
        logger.debug("Getting translation with fallback for entityType={}, entityId={}, fieldName={}, languageCode={}", 
            entityType, entityId, fieldName, languageCode);
        
        String translation = getTranslation(entityType, entityId, fieldName, languageCode);
        if (translation == null && !"en-US".equals(languageCode)) {
            logger.debug("Translation not found for language {}, falling back to en-US", languageCode);
            translation = getTranslation(entityType, entityId, fieldName, "en-US");
        }
        
        return translation;
    }
}

