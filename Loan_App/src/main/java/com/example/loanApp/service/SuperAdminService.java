package com.example.loanApp.service;


import com.example.loanApp.enums.TransactionMethod;
import com.example.loanApp.payload.ResponseJSON;
import org.springframework.http.ResponseEntity;

public interface SuperAdminService {
    ResponseEntity<ResponseJSON> handleCreateTransactionMethod(TransactionMethod name);

    ResponseEntity<ResponseJSON> handleGetTransactionMethod(Long id);

    ResponseEntity<ResponseJSON> handleUpdateTransactionMethod(Long id, TransactionMethod name);

    ResponseEntity<ResponseJSON> handleDeleteTransactionMethod(Long id);
}
