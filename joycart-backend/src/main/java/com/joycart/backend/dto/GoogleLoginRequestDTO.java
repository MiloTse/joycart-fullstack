package com.joycart.backend.dto;

/**
 * Google登录请求DTO
 * 用于接收前端发送的Google ID Token
 */
public class GoogleLoginRequestDTO {
    
    /**
     * Google ID Token
     * 由Google Identity Services返回的ID Token字符串
     */
    private String idToken;

    public GoogleLoginRequestDTO() {
    }

    public GoogleLoginRequestDTO(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "GoogleLoginRequestDTO{" +
                "idToken='" + (idToken != null && idToken.length() > 20 
                    ? idToken.substring(0, 20) + "..." : idToken) + '\'' +
                '}';
    }
}

