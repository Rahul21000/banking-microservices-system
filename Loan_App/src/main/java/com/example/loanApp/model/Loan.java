package com.example.loanApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long loanId;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private int tenureMonths;
    private BigDecimal emiAmount;
    private LocalDate loanStartDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_no", referencedColumnName = "accountNo", nullable = false)
    private Account account;


    @PrePersist
    void prePersist(){
        this.loanStartDate = LocalDate.now();
    }
//    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
//    private List<Transaction> transactions;

}
