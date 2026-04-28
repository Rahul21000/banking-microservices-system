package com.example.loanApp.model;

import com.example.loanApp.enums.AccountType;
import com.example.loanApp.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "account")
//@Table(name = "account",indexes = @Index(name = "idx_account_no", columnList = "accountNo"))
//@EqualsAndHashCode(of = "accountNo")  // Optionally, use to control equality based on accountN
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankName;
    @Column(unique = true)
    private Long accountNo;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Enumerated(EnumType.STRING)
    private Status accountStatus;
    private LocalDate openingDate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
//    @JsonIgnore
//    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Transaction> transactions;

    @PrePersist
    public void prePersist() {
        this.openingDate = LocalDate.now();
    }

}
