package com.joycart.backend.service.impl;

import com.joycart.backend.config.DeliveryTimeConfig;
import com.joycart.backend.service.DeliveryTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DeliveryTimeServiceImpl implements DeliveryTimeService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryTimeServiceImpl.class);
    
    @Autowired
    private DeliveryTimeConfig config;

    @Override
    public List<List<Map<String, String>>> getAvailableDeliveryTimes() {
        logger.debug("Generating available delivery times");
        
        List<List<Map<String, String>>> timeRange = new ArrayList<>();

        List<Map<String, String>> dateOptions = generateDateOptions();
        List<Map<String, String>> hourOptions = generateHourOptions();
        List<Map<String, String>> minuteOptions = generateMinuteOptions();
        
        timeRange.add(dateOptions);
        timeRange.add(hourOptions);
        timeRange.add(minuteOptions);
        
        logger.info("Generated delivery time options: {} dates, {} hours, {} minutes", 
                   dateOptions.size(), hourOptions.size(), minuteOptions.size());
        
        return timeRange;
    }


    /**
     * 生成日期选项（基于配置的天数）
     */
    private List<Map<String, String>> generateDateOptions() {
        List<Map<String, String>> dateOptions = new ArrayList<>();
        LocalDate today = LocalDate.now();
        
        for (int i = 1; i <= config.getAvailableDays(); i++) {
            LocalDate date = today.plusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            // 检查是否在排除日期列表中
            if (config.getExcludedDates().contains(dateStr)) {
                logger.debug("Skipping excluded date: {}", dateStr);
                continue;
            }
            
            Map<String, String> dateOption = new HashMap<>();
            dateOption.put("label", dateStr);
            dateOption.put("value", dateStr);
            dateOptions.add(dateOption);
        }
        
        return dateOptions;
    }
    
    /**
     * 生成小时选项（基于配置的营业时间）
     */
    private List<Map<String, String>> generateHourOptions() {
        List<Map<String, String>> hourOptions = new ArrayList<>();
        
        for (int hour = config.getStartHour(); hour <= config.getEndHour(); hour++) {
            Map<String, String> hourOption = new HashMap<>();
            String hourStr = String.format("%02d", hour);
            hourOption.put("label", hourStr);
            hourOption.put("value", hourStr);
            hourOptions.add(hourOption);
        }
        
        return hourOptions;
    }
    
    /**
     * 生成分钟选项（基于配置的时间间隔）
     */
    private List<Map<String, String>> generateMinuteOptions() {
        List<Map<String, String>> minuteOptions = new ArrayList<>();
        
        for (int minute = 0; minute < 60; minute += config.getSlotMinutes()) {
            Map<String, String> minuteOption = new HashMap<>();
            String minuteStr = String.format("%02d", minute);
            minuteOption.put("label", minuteStr);
            minuteOption.put("value", minuteStr);
            minuteOptions.add(minuteOption);
        }
        
        return minuteOptions;
    }
}
