package com.tramell.cinesphere.exception;

import com.tramell.cinesphere.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = ApiResponse.builder().success(false).message(ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiResponse<Object> response = ApiResponse.builder().success(false).message("Endpoint not found: " + ex.getResourcePath()).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException ex) {
        ApiResponse<Object> response = ApiResponse.builder().success(false).message(ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<ApiResponse<Object>> handleSeatAlreadyBookedException(SeatAlreadyBookedException ex) {
        ApiResponse<Object> response = ApiResponse.builder().success(false).message(ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // Or BAD_REQUEST depending on preference
    }
    
    @ExceptionHandler(CouponExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleCouponExpiredException(CouponExpiredException ex) {
        ApiResponse<Object> response = ApiResponse.builder().success(false).message(ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<Object> response = ApiResponse.builder().success(false).message("Validation failed").data(errors).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }




    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameter = ex.getName();
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("Invalid value for parameter: " + parameter)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String parameter = ex.getParameterName();
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("Missing required parameter: " + parameter)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleMalformedJson(HttpMessageNotReadableException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("Malformed or invalid request body")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("Invalid email or password")
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }



    @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message("The request conflicts with existing data")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        ApiResponse<Object> response = ApiResponse.builder().success(false).message("An unexpected error occurred: " + ex.getMessage()).build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
