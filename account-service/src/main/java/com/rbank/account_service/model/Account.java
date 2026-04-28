package com.rbank.account_service.model;

import com.rbank.account_service.enums.AccountType;
import com.rbank.account_service.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity(name = "account")
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private Long customerId;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false,unique = true)
    private Long accountNo;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status accountStatus;

    private LocalDate openingDate;

    @PrePersist
    public void prePersist() {
        this.openingDate = LocalDate.now();
    }

}
