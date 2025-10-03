package com.joycart.backend.repository;

import com.joycart.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据productId查找激活的商品
     * @param productId 商品ID
     * @return 商品信息
     */
    Optional<Product> findByProductIdAndIsActiveTrue(String productId);
    
    /**
     * 查找所有激活的商品
     * @return 激活的商品列表
     */
    List<Product> findByIsActiveTrue();
}
