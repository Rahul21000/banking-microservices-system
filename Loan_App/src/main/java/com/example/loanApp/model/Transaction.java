package com.example.loanApp.model;

import com.example.loanApp.enums.TransactionMethod;
import com.example.loanApp.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "customer_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;
    private LocalDate transactionDate;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal totalBalance;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_no", referencedColumnName = "accountNo", nullable = false)
    private Account account;

    @PrePersist
    public void prePersist() {
        if (transactionDate == null) {
            this.transactionDate = LocalDate.now();
        }
    }

}
