package com.rbank.account_service.utils;
import com.rbank.account_service.dto.ResponseJson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtils {
    public ResponseEntity<ResponseJson> handleResponseInPayload(Object body, String message, int statusCode, HttpStatus status){
        ResponseJson responseJson = new ResponseJson();
        responseJson.setMessage(message);
        responseJson.setStatusCode(statusCode);
        responseJson.setStatus(status);
        responseJson.setBody(body);
        return new ResponseEntity<>(responseJson, status);
    }
}
