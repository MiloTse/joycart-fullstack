package com.joycart.backend.repository;

import com.joycart.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    

    Optional<Cart> findByUserIdAndProductIdAndIsActiveTrue(Integer userId, String productId);
    
    List<Cart> findByUserIdAndIsActiveTrue(Integer userId);
}
