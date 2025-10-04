package com.joycart.backend.repository;

import com.joycart.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    /**
     * 查找所有激活的位置，按排序字段排序
     */
    List<Location> findByIsActiveTrueOrderBySortOrderAscIdAsc();
    
    /**
     * 根据位置ID查找激活的位置
     */
    Location findByLocationIdAndIsActiveTrue(String locationId);
}
