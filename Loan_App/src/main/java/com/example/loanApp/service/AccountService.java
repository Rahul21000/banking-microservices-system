package com.example.loanApp.service;

import com.example.loanApp.enums.AccountType;
import com.example.loanApp.enums.Status;
import com.example.loanApp.model.Account;
import com.example.loanApp.model.Customer;

public interface AccountService {
    Account handleCreateAccount(Customer customer, AccountType accountType);
    Account getAccountByCustomerId(Long customerId);
    void changeAccountStatus(Long customerId, Status status);
}
