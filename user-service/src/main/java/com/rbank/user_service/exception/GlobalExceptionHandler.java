package com.rbank.user_service.exception;

import com.rbank.user_service.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGeneralException(Exception e) { // Use Exception here
        ResponseDTO responseMessageJSON = new ResponseDTO();
        responseMessageJSON.setMessage(e.getMessage());
        responseMessageJSON.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        responseMessageJSON.setStatus("INTERNAL SERVER ERROR");
        return new ResponseEntity<>(responseMessageJSON, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<ResponseDTO> handleCustomerAlreadyExistException(CustomerAlreadyExistException e) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.CONFLICT.value());
        responseDTO.setStatus("CONFLICT");
        return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleCustomerAlreadyExistException(CustomerNotFoundException e) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.NOT_FOUND.value());
        responseDTO.setStatus("NOT FOUND");
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleCustomerAlreadyExistException(ResourceNotFoundException e) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        responseDTO.setStatus("BAD REQUEST");
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

}
