package com.joycart.backend.service;

import java.util.List;
import java.util.Map;

public interface DeliveryTimeService {
    
    /**
     * 获取可选的配送时间选项
     * @return 时间选择器数据格式：[[日期选项], [小时选项], [分钟选项]]
     */
    List<List<Map<String, String>>> getAvailableDeliveryTimes();

}
