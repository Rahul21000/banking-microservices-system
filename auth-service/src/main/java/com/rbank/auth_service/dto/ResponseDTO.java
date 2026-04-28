package com.rbank.auth_service.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public final class ResponseDTO {
    private String message;
    private int statusCode;
    private String status;
    private Object body;
}
