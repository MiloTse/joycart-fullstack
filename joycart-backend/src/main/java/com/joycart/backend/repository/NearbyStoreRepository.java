package com.joycart.backend.repository;

import com.joycart.backend.model.NearbyStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NearbyStoreRepository extends JpaRepository<NearbyStore, Long> {
    
    /**
     * 查找所有激活的商店，按排序字段排序
     * @return 激活的商店列表
     */
    List<NearbyStore> findByIsActiveTrueOrderBySortOrderAsc();
    
    /**
     * 根据storeId查找激活的商店
     * @param storeId 商店ID
     * @return 商店信息
     */
    Optional<NearbyStore> findByStoreIdAndIsActiveTrue(String storeId);
}
