package com.joycart.backend.dto;

public class LoginResponseDTO {
    private String status;
    private UserData data;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String status, UserData data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public static class UserData {
        private int id;
        private String token;

        public UserData() {
        }

        public UserData(int id, String token) {
            this.id = id;
            this.token = token;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}