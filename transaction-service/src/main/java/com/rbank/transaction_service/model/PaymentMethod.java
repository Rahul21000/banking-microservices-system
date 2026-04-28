package com.rbank.transaction_service.model;

import com.rbank.transaction_service.enums.TransactionMethod;
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
