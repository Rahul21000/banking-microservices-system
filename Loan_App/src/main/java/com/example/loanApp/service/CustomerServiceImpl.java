package com.example.loanApp.service;

import com.example.loanApp.dto.CustomerDTO;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.exception.CustomerAlreadyExistsException;
import com.example.loanApp.model.Account;
import com.example.loanApp.model.Customer;
import com.example.loanApp.model.Transaction;
import com.example.loanApp.payload.ResponseUtils;
import com.example.loanApp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ResponseUtils responseUtils;


    public ResponseEntity<ResponseJSON> handleCreateAccount(CustomerDTO customerDTO){
        Optional<Customer> existCustomer = customerRepository.findByMobile(customerDTO.getMobile());
        if(existCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exist");
        }
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setMobile(customerDTO.getMobile());
        Customer newCustomer = customerRepository.save(customer);
        Account newAccount = accountService.handleCreateAccount(newCustomer, customerDTO.getAccountType());
        Transaction newTransaction = transactionService.handleInitialOpeningTransaction(newAccount);
        Map<String,Object> lhs = new LinkedHashMap<>();
        lhs.put("customer",newCustomer);
        lhs.put("account",newAccount);
        lhs.put("transaction",newTransaction);
        return responseUtils.handleResponseInPayload(lhs,"Account created Successfully",201, HttpStatus.CREATED);
    }
}
