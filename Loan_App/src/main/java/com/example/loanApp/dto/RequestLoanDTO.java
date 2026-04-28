package com.example.loanApp.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestLoanDTO {
    private Long CustomerId;
    private BigDecimal principalAmount;
    private BigDecimal annualInterestRate;
    private Integer tenureMonths;
}
