package com.rbank.auth_service.utils;

import com.rbank.auth_service.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtils {
    public ResponseEntity<ResponseDTO> handleResponseInPayload(Object body, String message, int statusCode, HttpStatus status){
        ResponseDTO ResponseDTO = new ResponseDTO();
        ResponseDTO.setMessage(message);
        ResponseDTO.setStatusCode(statusCode);
        ResponseDTO.setStatus(status.getReasonPhrase());
        ResponseDTO.setBody(body);
        return new ResponseEntity<>(ResponseDTO, status);
    }

}
