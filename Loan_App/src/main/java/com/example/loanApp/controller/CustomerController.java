package com.example.loanApp.controller;

import com.example.loanApp.dto.CustomerDTO;
import com.example.loanApp.dto.transaction.RequestTransactionDTO;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.service.CustomerService;
import com.example.loanApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create_account")
    ResponseEntity<ResponseJSON> createAccount(@RequestBody CustomerDTO customerDTO){
     try{
        return customerService.handleCreateAccount(customerDTO);
     } catch (Exception e){
         System.out.println(e.getMessage());
         throw e;
     }
    }


    @PostMapping("/request_Change_account_type")
    ResponseEntity<ResponseJSON> requestForChangeAccount(@RequestBody CustomerDTO customerDTO){
        try{
            return customerService.handleCreateAccount(customerDTO);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }


    @PostMapping("/delete_account")
    ResponseEntity<ResponseJSON> closeAccount(@PathVariable Long customerID){
        try{
            return transactionService.handleCloseAccount(customerID);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @PostMapping("/partner_with_bank")
    ResponseEntity<ResponseJSON>partner_with_bank (@RequestBody RequestTransactionDTO requestTransactionDTO){
        try{
            return transactionService.handleWithdrawalAmount(requestTransactionDTO);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
