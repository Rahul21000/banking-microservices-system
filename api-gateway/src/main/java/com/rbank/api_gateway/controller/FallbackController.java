package com.rbank.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public ResponseEntity<String> authServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Authentication Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("User Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/accounts")
    public ResponseEntity<String> accountServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Account Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/transactions")
    public ResponseEntity<String> transactionServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Transaction Service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/loans")
    public ResponseEntity<String> loanServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Loan Service is temporarily unavailable. Please try again later.");
    }
}
