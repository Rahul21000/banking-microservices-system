package com.rbank.account_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception e) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("message",e.getMessage());
        errors.put("statusCode",String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        errors.put("status",String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, String>> handleInvalidInputException(InvalidInputException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("message",e.getMessage());
        errors.put("statusCode",String.valueOf(HttpStatus.BAD_REQUEST.value()));
        errors.put("status",String.valueOf(HttpStatus.BAD_REQUEST));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String,String> errors = new HashMap<>();
        for ( FieldError error : fieldErrors){
            errors.put(error.getField(),error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientAmountException.class)
    public ResponseEntity<Map<String,String>> handleInsufficientAmountException(InsufficientAmountException e) { // Correct parameter type
        Map<String,String> error = new HashMap<>();
        error.put("message",e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
