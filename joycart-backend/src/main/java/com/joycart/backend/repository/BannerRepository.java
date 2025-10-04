package com.joycart.backend.repository;

import com.joycart.backend.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    
    /**
     * 查找所有激活的轮播图，按排序字段排序
     */
    List<Banner> findByIsActiveTrueOrderBySortOrderAscIdAsc();
    
    /**
     * 根据轮播图ID查找激活的轮播图
     */
    Banner findByBannerIdAndIsActiveTrue(String bannerId);
}
