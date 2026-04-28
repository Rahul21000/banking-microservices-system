package com.rbank.account_service.repository;

import com.rbank.account_service.dto.AccountStatusDTO;
import com.rbank.account_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

   @Query(value = "SELECT * FROM account WHERE customer_id = :customerId",nativeQuery = true)
   Optional<Account> findAccountByCustomerId(@Param("customerId") Long customerId);

   @Query(value = "SELECT account_no,status FROM account WHERE customer_id = :customerId",nativeQuery = true)
   AccountStatusDTO findAccountNoAndStatusByCustomerId(@Param("customerId") Long customerId);

   @Query(value = "DELETE account WHERE customer_id = :customerId",nativeQuery = true)
   void deleteAccountByCustomerId(Long customerId);
}
