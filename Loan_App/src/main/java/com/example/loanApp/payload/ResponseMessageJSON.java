package com.example.loanApp.payload;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseMessageJSON {
    private String message;
    private int statusCode;
    private HttpStatus Status;
}
