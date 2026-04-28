package com.example.loanApp.service;

import com.example.loanApp.enums.AccountType;
import com.example.loanApp.enums.Status;
import com.example.loanApp.model.Account;
import com.example.loanApp.model.Customer;
import com.example.loanApp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static com.example.loanApp.enums.Status.DE_ACTIVE;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;
    private static final int BRANCH_CODE = 12345;
    private static final String BANK_NAME =  "R Loan";


    public Account handleCreateAccount(Customer customer,AccountType  accountType){
        Account account = new Account();
        account.setBankName(BANK_NAME);
        account.setAccountStatus(Status.ACTIVE);
        account.setAccountNo(handleGenerateUniqueAccountNumber());
        if(accountType.equals(AccountType.SAVING)){
            account.setAccountType(AccountType.SAVING);
        }
        if(accountType.equals(AccountType.CURRENT)){
            account.setAccountType(AccountType.CURRENT);
        }
        account.setCustomer(customer);
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByCustomerId(Long customerId) {
        Optional<Account> existAccount = accountRepository.findAccountNoAndStatusByCustomerId(customerId);
        if(existAccount.isEmpty()){
            throw new RuntimeException("Account not find");
        }
        return existAccount.get();
    }

    @Override
    public void changeAccountStatus(Long customerId, Status status) {
        Account account = getAccountByCustomerId(customerId);
        switch (status){
            case DE_ACTIVE :
                account.setAccountStatus(DE_ACTIVE);
                break;
            case CLOSE :
                account.setAccountStatus(Status.CLOSE);
                break;
            case PENDING :
                account.setAccountStatus(Status.PENDING);
                break;
            default:
                throw new IllegalArgumentException("Invalid type: ");
        }
       accountRepository.save(account);
    }

    private static Long handleGenerateUniqueAccountNumber() {
        Random random = new Random();
        int firstDigit = random.nextInt(9) + 1;
        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(firstDigit);
        for (int i = 1; i < 10; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return Long.parseLong(accountNumber.toString());
    }
}
