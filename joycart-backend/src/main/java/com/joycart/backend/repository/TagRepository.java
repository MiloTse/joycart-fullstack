package com.joycart.backend.repository;

import com.joycart.backend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    /**
     * 查找所有激活的标签，按排序字段排序
     * @return 激活的标签列表
     */
    @Query("SELECT t FROM Tag t WHERE t.isActive = true ORDER BY t.sortOrder ASC, t.id ASC")
    List<Tag> findAllActiveOrderBySortOrder();
    
}
