package com.joycart.backend.repository;

import com.joycart.backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * 根据订单ID查找订单
     * @param orderId 订单ID
     * @return 订单信息
     */
    Optional<Order> findByOrderId(String orderId);
    
    /**
     * 根据用户ID查找订单列表
     * @param userId 用户ID
     * @return 订单列表
     */
    java.util.List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);
}
