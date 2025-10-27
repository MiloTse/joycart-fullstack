package com.joycart.backend.repository;

import com.joycart.backend.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    
    /**
     * 获取特定实体的所有字段翻译
     */
    List<Translation> findByEntityTypeAndEntityIdAndLanguageCode(
        String entityType, Long entityId, String languageCode
    );
    
    /**
     * 获取特定实体的所有语言翻译
     */
    List<Translation> findByEntityTypeAndEntityId(String entityType, Long entityId);
}

