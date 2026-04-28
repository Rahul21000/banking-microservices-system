package com.rbank.transaction_service.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public final class ResponseJson {
    private String message;
    private int statusCode;
    private HttpStatus Status;
    private Object body;
}
