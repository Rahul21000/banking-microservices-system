package com.rbank.user_service.service;


import com.rbank.user_service.dto.CustomerAuthDTO;
import com.rbank.user_service.dto.CustomerDTO;
import com.rbank.user_service.dto.ResponseDTO;
import com.rbank.user_service.exception.CustomerNotFoundException;
import com.rbank.user_service.exception.ResourceNotFoundException;
import com.rbank.user_service.model.Customer;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<ResponseDTO> handleRegisterCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException, ResourceNotFoundException;

    Customer getCustomerById(Long id) throws CustomerNotFoundException;

    CustomerAuthDTO handleGetCustomerWithRoles(String username) throws CustomerNotFoundException;
}
