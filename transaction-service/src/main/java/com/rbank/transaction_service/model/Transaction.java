package com.rbank.transaction_service.model;

import com.rbank.transaction_service.enums.TransactionMethod;
import com.rbank.transaction_service.enums.TransactionType;
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

    @Column(nullable = false,unique = true)
    private Long accountNo;

    @Column(nullable = false,unique = true)
    private String transactionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionMethod transactionMethod;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal totalBalance;


    @PrePersist
    public void prePersist() {
        if (transactionDate == null) {
            this.transactionDate = LocalDate.now();
        }
    }

}
