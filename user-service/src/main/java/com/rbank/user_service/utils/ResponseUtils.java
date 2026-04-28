package com.rbank.user_service.utils;
import com.rbank.user_service.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtils {
    public ResponseEntity<ResponseDTO> handleResponseInPayload(Object body, String message, int statusCode, HttpStatus status){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage(message);
        responseDTO.setStatusCode(statusCode);
        responseDTO.setStatus(status.getReasonPhrase());
        responseDTO.setBody(body);
        return new ResponseEntity<>(responseDTO, status);
    }
}
