package com.example.loanApp.model;

import com.example.loanApp.enums.TransactionMethod;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "Payment_method")
@Data
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransactionMethod name;
}
