package com.example.loanApp.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtils {

    public ResponseEntity<ResponseJSON> handleResponseInPayload(Object body, String message, int statusCode, HttpStatus status){
        ResponseJSON responseJSON = new ResponseJSON();
        responseJSON.setBody(body);
        responseJSON.setMessage(message);
        responseJSON.setStatusCode(statusCode);
        responseJSON.setStatus(status);
        return new ResponseEntity<>(responseJSON,status);
    }

}
