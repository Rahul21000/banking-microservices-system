package com.rbank.transaction_service.dto;

import com.rbank.transaction_service.enums.TransactionMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;

@Data
public final class RequestTransactionDTO {
    private Long customerId;
    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;
    private BigDecimal amount;

}
