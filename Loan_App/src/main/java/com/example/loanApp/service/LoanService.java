package com.example.loanApp.service;

import com.example.loanApp.dto.RequestLoanDTO;
import com.example.loanApp.payload.ResponseJSON;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface LoanService {
    ResponseEntity<ResponseJSON> createLoan(RequestLoanDTO requestLoanDTO);

    ResponseEntity<ResponseJSON> handleLoanPrediction(BigDecimal principalAmount, BigDecimal interestRate, Integer tenureMonths);

    ResponseEntity<ResponseJSON> creditNormalization(Long userId);

    ResponseEntity<ResponseJSON> CreditScore(Long customerId);
}
