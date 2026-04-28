package com.example.loanApp.service;

import com.example.loanApp.dto.transaction.RequestTransactionDTO;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.exception.InsufficientAmountException;
import com.example.loanApp.model.Account;
import com.example.loanApp.model.Transaction;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ResponseJSON> handleDepositAmount(RequestTransactionDTO requestTransactionDTO) throws RuntimeException;
    ResponseEntity<ResponseJSON> handleWithdrawalAmount(RequestTransactionDTO requestTransactionDTO) throws InsufficientAmountException,RuntimeException;
    ResponseEntity<ResponseJSON> handleCheckBalance(Long UserId);
    Transaction handleInitialOpeningTransaction(Account account);

    ResponseEntity<ResponseJSON> handleCloseAccount(Long customerID);
}
