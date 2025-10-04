package com.joycart.backend.repository;

import com.joycart.backend.model.HotSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotSearchRepository extends JpaRepository<HotSearch, Long> {
    
    /**
     * 查找所有激活的热门搜索，按排序字段排序
     */
    List<HotSearch> findByIsActiveTrueOrderBySortOrderAscIdAsc();
    
    /**
     * 根据搜索ID查找激活的热门搜索
     */
    HotSearch findBySearchIdAndIsActiveTrue(String searchId);
    
    /**
     * 根据关键词查找激活的热门搜索
     */
    HotSearch findByKeywordAndIsActiveTrue(String keyword);
}
