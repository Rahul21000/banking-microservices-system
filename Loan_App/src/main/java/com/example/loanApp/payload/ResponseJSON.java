package com.example.loanApp.payload;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public final class ResponseJSON {
    private String message;
    private int statusCode;
    private HttpStatus Status;
    private Object body;
}
