package com.rbank.transaction_service.repository;

import com.rbank.transaction_service.enums.TransactionMethod;
import com.rbank.transaction_service.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionMethodRepository extends JpaRepository<PaymentMethod,Long> {
    boolean existsByName(TransactionMethod name);
    Optional<PaymentMethod> getTransactionMethodByName(TransactionMethod name);
}
