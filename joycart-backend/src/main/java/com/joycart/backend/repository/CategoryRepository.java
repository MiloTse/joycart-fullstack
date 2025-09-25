package com.joycart.backend.repository;

import com.joycart.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * 查找所有激活的分类，按排序字段排序
     * @return 激活的分类列表
     */
    @Query("SELECT c FROM Category c WHERE c.isActive = true ORDER BY c.sortOrder ASC, c.id ASC")
    List<Category> findAllActiveOrderBySortOrder();
    
}
