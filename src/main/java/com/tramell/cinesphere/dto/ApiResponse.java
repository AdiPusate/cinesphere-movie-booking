package com.tramell.cinesphere.dto;

import java.time.LocalDateTime;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String message, T data, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private boolean success;
        private String message;
        private T data;
        private LocalDateTime timestamp;

        public ApiResponseBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiResponse<T> build() {
            return new ApiResponse(this.success, this.message, this.data, this.timestamp);
        }
    }
}
