package com.example.loanApp.repository;

import com.example.loanApp.enums.TransactionMethod;
import com.example.loanApp.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionMethodRepository extends JpaRepository<PaymentMethod,Long> {
    boolean existsByName(TransactionMethod name);
    Optional<PaymentMethod> getTransactionMethodByName(TransactionMethod name);
}
