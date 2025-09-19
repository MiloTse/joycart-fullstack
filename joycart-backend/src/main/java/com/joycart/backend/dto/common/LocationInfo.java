package com.joycart.backend.dto.common;

/**
 * 位置信息DTO
 * 可复用于：用户地址、门店位置、配送地址、订单地址等
 */
public class LocationInfo {
    private String id;
    private String address;
    private Double latitude;    // 纬度
    private Double longitude;   // 经度
    private String type;        // 地址类型：home/work/store/delivery
    private String name;        // 位置名称：如"家"、"公司"、"XX门店"


    public LocationInfo() {
    }

    public LocationInfo(String id, String address) {
        this.id = id;
        this.address = address;
    }

    public LocationInfo(String id, String address, Double latitude, Double longitude, String type, String name) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
