package com.rbank.account_service.controller;

import com.rbank.account_service.dto.AccountStatusDTO;
import com.rbank.account_service.dto.ResponseJson;
import com.rbank.account_service.model.Account;
import com.rbank.account_service.service.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    public AccountService accountService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @PostMapping("/create")
    ResponseEntity<ResponseJson> createAccount(@Valid @RequestBody AccountStatusDTO requestDto){
        LOGGER.info("create account Method triggered");
     try{
        return accountService.handleCreateAccount(requestDto);
     } catch (Exception e){
         System.out.println(e.getMessage());
         throw e;
     }
    }

    @GetMapping("/{id}")
    ResponseEntity<Account> getAccountById(@PathVariable Long id){
        LOGGER.info("Get account Method triggered");
        try{
           Account account = accountService.getAccountById(id);
            return ResponseEntity.status(200).body(account);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @GetMapping("getAccountNoAndStatus/{customerId}")
     AccountStatusDTO getAccountNoAndStatusByCustomerId(@PathVariable Long customerId){
        LOGGER.info("Get account by customer id Method triggered");
        try{
            return accountService.handleGetAccountNoAndStatusByCustomerId(customerId);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @GetMapping("getAccountDetails/{customerId}")
    ResponseEntity<Account> getAccountDetailsByCustomer(@PathVariable Long customerId){
        LOGGER.info("Get account details by customer id Method triggered");
        try{
            Account account = accountService.getAccountDetailsByCustomerId(customerId);
            return ResponseEntity.status(200).body(account);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseJson> deleteAccount(@PathVariable Long id) {
        LOGGER.info("Delete account Method triggered");
        try {
            return accountService.deleteAccount(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/request-change-account-type/{customerId}")
    ResponseEntity<ResponseJson> requestForChangeAccount(@PathVariable("customerId") Long customerId){
        try{
            return accountService.handleRequestChangeAccount(customerId);
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @PutMapping("/active-status/{customerId}")
    ResponseEntity<ResponseJson> activeAccountStatus(@PathVariable("customerId") Long customerID){
        try{
            return accountService.handleActiveAccountStatus(customerID);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/de-active-status/{customerId}")
    ResponseEntity<ResponseJson> deActiveAccountStatus(@PathVariable("customerId") Long customerID){
        try{
            return accountService.handleDeActiveAccountStatus(customerID);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("close/{id}")
    ResponseEntity<ResponseJson> closeAccount(@PathVariable Long id){
        LOGGER.info("Close account Method triggered");
        try{
            return accountService.handleCloseAccount(id);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
