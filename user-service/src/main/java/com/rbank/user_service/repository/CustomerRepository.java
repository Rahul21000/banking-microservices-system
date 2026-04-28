package com.rbank.user_service.repository;



import com.rbank.user_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByMobile(String mobile);

    @Query("SELECT c FROM Customer c JOIN FETCH c.roles WHERE c.username = :username")
    Optional<Customer> findByUsernameWithRoles(@Param("username") String username);
}
