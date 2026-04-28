package com.example.loanApp.controller;

import com.example.loanApp.dto.CustomerDTO;
import com.example.loanApp.dto.transaction.RequestTransactionDTO;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/withdrawal_amount/nearest_branch")
    ResponseEntity<ResponseJSON> requestForWithdrawalNearestBranch(@RequestBody CustomerDTO customerDTO){
//        try{
//            return customerService.handleCreateAccount(customerDTO);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//            throw e;
//        }
        return null;
    }

    @PostMapping("/deposit_amount")
    ResponseEntity<ResponseJSON> depositAmount(@RequestBody RequestTransactionDTO requestTransactionDTO){
        System.out.println("test");
        try{
            return transactionService.handleDepositAmount(requestTransactionDTO);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/withdrawal_amount")
    ResponseEntity<ResponseJSON> withdrawalAmount(@RequestBody RequestTransactionDTO requestTransactionDTO){
        try{
            return transactionService.handleWithdrawalAmount(requestTransactionDTO);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
