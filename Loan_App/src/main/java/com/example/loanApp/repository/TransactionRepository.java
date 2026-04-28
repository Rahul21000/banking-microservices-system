package com.example.loanApp.repository;

import com.example.loanApp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT total_balance FROM customer_transaction WHERE account_no = :accountNo"
            + " ORDER BY " +
            "id DESC LIMIT 1", nativeQuery = true)
    BigDecimal findTotalBalanceByAccountNo(@Param("accountNo") Long accountNo);

    List<Transaction> findByAccountAccountNoAndTransactionDateBetween(Long accountNo, LocalDate startDate, LocalDate endDate);
}
