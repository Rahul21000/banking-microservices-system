package com.example.loanApp.controller;

import com.example.loanApp.model.PaymentMethod;
import com.example.loanApp.payload.ResponseJSON;
import com.example.loanApp.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/super_admin_resource")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping("/transaction_method")
    ResponseEntity<ResponseJSON> createTransactionMethod(@RequestBody PaymentMethod transactionMethod){
        return superAdminService.handleCreateTransactionMethod(transactionMethod.getName());
    }

    @GetMapping("/transaction_method/{id}")
    ResponseEntity<ResponseJSON> createTransactionMethod(@PathVariable Long id){
        return superAdminService.handleGetTransactionMethod(id);
    }

    @PutMapping("/transaction_method/{id}")
    ResponseEntity<ResponseJSON> createTransactionMethod(@PathVariable("id") Long id,@RequestBody PaymentMethod transactionMethod){
        return superAdminService.handleUpdateTransactionMethod(id,transactionMethod.getName());
    }

    @DeleteMapping("/transaction_method/{id}")
    ResponseEntity<ResponseJSON> deleteTransactionMethod(@PathVariable("id") Long id){
        return superAdminService.handleDeleteTransactionMethod(id);
    }
}
