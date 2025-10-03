package com.joycart.backend.repository;

import com.joycart.backend.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    
    /**
     * 根据用户ID查找所有激活的地址，按默认地址优先、创建时间倒序排列
     * @param userId 用户ID
     * @return 用户地址列表
     */
    List<UserAddress> findActiveAddrByUserId(Integer userId);

}
