package com.example.loanApp.dto.transaction;
import com.example.loanApp.enums.TransactionMethod;
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
