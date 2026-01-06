package com.joycart.backend.dto;

/**
 * Google用户信息DTO
 * 用于存储从Google ID Token中提取的用户信息
 */
public class GoogleUserInfo {
    
    private String email;
    private String name;
    private String picture;
    private String googleId; // Google用户ID (sub字段)
    private Boolean emailVerified;

    public GoogleUserInfo() {
    }

    public GoogleUserInfo(String email, String name, String picture, String googleId, Boolean emailVerified) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.googleId = googleId;
        this.emailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "GoogleUserInfo{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", picture='" + picture + '\'' +
                ", googleId='" + googleId + '\'' +
                ", emailVerified=" + emailVerified +
                '}';
    }
}

