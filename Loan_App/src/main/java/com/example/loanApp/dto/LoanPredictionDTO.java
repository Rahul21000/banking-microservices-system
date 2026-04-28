package com.example.loanApp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanPredictionDTO {
    private BigDecimal principalAmount;
    private BigDecimal annualInterestRate;
    private Integer tenureMonths;
}
