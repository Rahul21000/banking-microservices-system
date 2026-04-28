package com.example.loanApp.exception;

import com.example.loanApp.payload.ResponseMessageJSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessageJSON> handleException(Exception ex) { // Use Exception here
        ResponseMessageJSON responseMessageJSON = new ResponseMessageJSON();
        responseMessageJSON.setMessage(ex.getMessage());
        responseMessageJSON.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseMessageJSON.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseMessageJSON, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessageJSON> handleRuntimeException(RuntimeException ex) { // Use RuntimeException here
        ResponseMessageJSON responseMessageJSON = new ResponseMessageJSON();
        responseMessageJSON.setMessage(ex.getMessage());
        responseMessageJSON.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseMessageJSON.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseMessageJSON, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseMessageJSON> handleArgumentValidationException(IllegalArgumentException ex) { // Use IllegalArgumentException here
        ResponseMessageJSON responseMessageJSON = new ResponseMessageJSON();
        responseMessageJSON.setMessage(ex.getMessage());
        responseMessageJSON.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseMessageJSON.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseMessageJSON, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ResponseMessageJSON> handleCustomerAlreadyExists(CustomerAlreadyExistsException ex) { // Correct parameter type
        ResponseMessageJSON responseMessageJSON = new ResponseMessageJSON();
        responseMessageJSON.setMessage(ex.getMessage());
        responseMessageJSON.setStatusCode(HttpStatus.FOUND.value());
        responseMessageJSON.setStatus(HttpStatus.FOUND);
        return new ResponseEntity<>(responseMessageJSON, HttpStatus.FOUND);
    }

    @ExceptionHandler(InsufficientAmountException.class)
    public ResponseEntity<ResponseMessageJSON> handleInsufficientAmountException(InsufficientAmountException ex) { // Correct parameter type
        ResponseMessageJSON responseMessageJSON = new ResponseMessageJSON();
        responseMessageJSON.setMessage(ex.getMessage());
        responseMessageJSON.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseMessageJSON.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(responseMessageJSON, HttpStatus.BAD_REQUEST);
    }
}
