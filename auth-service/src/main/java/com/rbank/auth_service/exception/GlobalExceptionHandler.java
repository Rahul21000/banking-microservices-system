package com.rbank.auth_service.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbank.auth_service.dto.ResponseDTO;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception e) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("message",e.getMessage());
        errors.put("statusCode",HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("status","INTERNAL SERVER ERROR");
        errors.put("timeStamp", System.currentTimeMillis());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String,String>> handleMethodNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->errors.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidInputException(InvalidInputException e) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("message",e.getMessage());
        errors.put("statusCode",HttpStatus.BAD_REQUEST.value());
        errors.put("status","BAD_REQUEST");
        errors.put("timeStamp", System.currentTimeMillis());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String,Object>> handleFeignException(FeignException e){
        try {
            String responseBody = e.contentUTF8();
            JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
            String errorMessage = jsonNode.has("message") ? jsonNode.get("message").asText() : "Unknown error from UserService";
            String status = jsonNode.has("status") ? jsonNode.get("status").asText() : "Service unavailable";
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("message", errorMessage);
            errorResponse.put("statusCode", e.status());
            errorResponse.put("status", status);
            errorResponse.put("timeStamp", Instant.now());

            return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.status()));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Failed to process Feign error response"));

        }
    }

}
