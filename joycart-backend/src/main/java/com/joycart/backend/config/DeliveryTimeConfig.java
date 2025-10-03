package com.joycart.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "delivery.time")
public class DeliveryTimeConfig {
    
    private int startHour = 9;
    private int endHour = 18;
    private int slotMinutes = 30;
    private int availableDays = 3;
    private List<String> excludedDates = new ArrayList<>();
    
    public DeliveryTimeConfig() {
        // 默认排除一些节假日
        excludedDates.add("2025-12-25");
        excludedDates.add("2026-01-01");
    }
    
    public int getStartHour() {
        return startHour;
    }
    
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    
    public int getEndHour() {
        return endHour;
    }
    
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    
    public int getSlotMinutes() {
        return slotMinutes;
    }
    
    public void setSlotMinutes(int slotMinutes) {
        this.slotMinutes = slotMinutes;
    }
    
    public int getAvailableDays() {
        return availableDays;
    }
    
    public void setAvailableDays(int availableDays) {
        this.availableDays = availableDays;
    }
    
    public List<String> getExcludedDates() {
        return excludedDates;
    }
    
    public void setExcludedDates(List<String> excludedDates) {
        this.excludedDates = excludedDates;
    }
}
