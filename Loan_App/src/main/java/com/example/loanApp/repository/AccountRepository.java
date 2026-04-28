package com.example.loanApp.repository;

import com.example.loanApp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

//   @Query(value = "SELECT account_no, account_type FROM account WHERE customer_id = :customerId",nativeQuery = true)
   @Query(value = "SELECT * FROM account WHERE customer_id = :customerId",nativeQuery = true)
   Optional<Account> findAccountNoAndStatusByCustomerId(@Param("customerId") Long customerId);

   @Query(value = "DELETE account WHERE customer_id = :customerId",nativeQuery = true)
   void deleteAccountByCustomerId(Long customerId);
}
