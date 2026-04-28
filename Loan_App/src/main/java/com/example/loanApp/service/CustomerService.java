package com.example.loanApp.service;

import com.example.loanApp.dto.CustomerDTO;
import com.example.loanApp.payload.ResponseJSON;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<ResponseJSON> handleCreateAccount(CustomerDTO customerDTO);
}
