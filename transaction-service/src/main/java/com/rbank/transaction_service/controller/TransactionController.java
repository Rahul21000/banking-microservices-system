package com.rbank.transaction_service.controller;

import com.rbank.transaction_service.dto.RequestTransactionDTO;
import com.rbank.transaction_service.dto.ResponseJson;
import com.rbank.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit_amount")
    ResponseEntity<ResponseJson> InitialOpeningTransaction(@RequestParam Long id){
        System.out.println("test");
        try{
            return transactionService.handleInitialOpeningTransaction(id);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/deposit_amount")
    ResponseEntity<ResponseJson> depositAmount(@RequestBody RequestTransactionDTO requestTransactionDTO){
        System.out.println("test");
        try{
            return transactionService.handleDepositAmount(requestTransactionDTO);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/check-balance")
    ResponseEntity<ResponseJson> checkBalance(@RequestBody Long customerId){
        try{
            return transactionService.handleCheckBalance(customerId);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/withdrawal_amount")
    ResponseEntity<ResponseJson> withdrawalAmount(@RequestBody RequestTransactionDTO requestTransactionDTO){
        try{
            return transactionService.handleWithdrawalAmount(requestTransactionDTO);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/withdrawal-amount/nearest-branch")
    ResponseEntity<ResponseJson> requestForWithdrawalNearestBranch(@RequestBody Long customerId){
//        try{
//            return customerService.handleCreateAccount(customerId);
//        } catch (Exception e){
//            System.out.println(e.getMessage());
//            throw e;
//        }
        return null;
    }

}
