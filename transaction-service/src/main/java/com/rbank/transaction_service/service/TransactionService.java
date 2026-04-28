package com.rbank.transaction_service.service;

import com.rbank.transaction_service.dto.RequestTransactionDTO;
import com.rbank.transaction_service.dto.ResponseJson;
import com.rbank.transaction_service.exception.InsufficientAmountException;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ResponseJson> handleDepositAmount(RequestTransactionDTO requestTransactionDTO) throws RuntimeException;
    ResponseEntity<ResponseJson> handleWithdrawalAmount(RequestTransactionDTO requestTransactionDTO) throws InsufficientAmountException,RuntimeException;
    ResponseEntity<ResponseJson> handleInitialOpeningTransaction(Long customerId);
    ResponseEntity<ResponseJson> handleCheckBalance(Long customerId);
}
