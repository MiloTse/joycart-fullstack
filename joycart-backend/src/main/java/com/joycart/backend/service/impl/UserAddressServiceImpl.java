package com.joycart.backend.service.impl;

import com.joycart.backend.model.UserAddress;
import com.joycart.backend.repository.UserAddressRepository;
import com.joycart.backend.service.UserAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    private static final Logger logger = LoggerFactory.getLogger(UserAddressServiceImpl.class);

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Override
    public List<Map<String, Object>> getUserAddresses(Integer userId) {
        logger.debug("Getting user addresses for userId: {}", userId);
        
        try {
            List<UserAddress> addresses = userAddressRepository.findActiveAddrByUserId(userId);
            logger.info("Found {} addresses for userId: {}", addresses.size(), userId);
            
            List<Map<String, Object>> addressList = new ArrayList<>();
            for (UserAddress address : addresses) {
                Map<String, Object> addressMap = new HashMap<>();
                addressMap.put("id", address.getId().toString());
                addressMap.put("name", address.getName());
                addressMap.put("phone", address.getPhone());
                addressMap.put("address", address.getAddress());
                addressMap.put("isDefault", address.getIsDefault());
                addressList.add(addressMap);
            }
            
            logger.info("Successfully retrieved {} addresses for userId: {}", addressList.size(), userId);
            return addressList;
            
        } catch (Exception e) {
            logger.error("Error getting user addresses for userId: {} - {}", userId, e.getMessage(), e);
            return null;
        }
    }
}
