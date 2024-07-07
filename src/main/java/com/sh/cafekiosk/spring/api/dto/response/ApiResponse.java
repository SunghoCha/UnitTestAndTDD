package com.sh.cafekiosk.spring.api.dto.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> { // ResponseEntity로는 실무에서 부족해서 커스텀해서 쓰는건가?

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    private ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
        return new ApiResponse<>(status, status.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return  new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), data);
    }
}
