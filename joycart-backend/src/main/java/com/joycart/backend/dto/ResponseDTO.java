package com.joycart.backend.dto;

import com.joycart.backend.constants.ApiConstants;

public class ResponseDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public ResponseDTO() {}

    public ResponseDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(ApiConstants.HTTP_STATUS_OK, ApiConstants.DEFAULT_RESPONSE_SUCCESS_MESSAGE, data);
    }

    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(ApiConstants.HTTP_STATUS_OK, message, data);
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(ApiConstants.HTTP_STATUS_BAD_REQUEST, message, null);
    }

    public static <T> ResponseDTO<T> error(Integer code, String message) {
        return new ResponseDTO<>(code, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}