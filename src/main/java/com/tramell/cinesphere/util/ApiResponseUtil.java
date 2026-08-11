package com.tramell.cinesphere.util;

import com.tramell.cinesphere.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, "Request completed successfully");
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return created(data, "Resource created successfully");
    }

    public static ResponseEntity<ApiResponse<Void>> deleted(String message) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
