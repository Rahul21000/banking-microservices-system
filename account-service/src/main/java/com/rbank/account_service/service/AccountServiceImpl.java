package com.rbank.account_service.service;

import com.rbank.account_service.dto.AccountStatusDTO;
import com.rbank.account_service.dto.ResponseJson;
import com.rbank.account_service.enums.AccountType;
import com.rbank.account_service.enums.Status;
import com.rbank.account_service.exception.InvalidInputException;
import com.rbank.account_service.exception.ResourceNotFoundException;
import com.rbank.account_service.model.Account;
import com.rbank.account_service.repository.AccountRepository;
import com.rbank.account_service.utils.GenerateAccountNumber;
import com.rbank.account_service.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ResponseUtils responseUtils;
    private static final int BRANCH_CODE = 12345;
    private static final String BANK_NAME =  "R Loan";
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);


    public ResponseEntity<ResponseJson> handleCreateAccount(AccountStatusDTO accountStatusDTO){
        LOGGER.info("For account creation customer id: {}", accountStatusDTO.getCustomerId());
        Account account = new Account();
        account.setBankName(BANK_NAME);
        account.setAccountStatus(Status.ACTIVE);
        account.setAccountNo(GenerateAccountNumber.handleGenerateUniqueAccountNumber());
        if(AccountType.SAVING.equals(accountStatusDTO.getStatus())){
            account.setAccountType(AccountType.SAVING);
        }
        if(AccountType.CURRENT.equals(accountStatusDTO.getStatus())){
            account.setAccountType(AccountType.CURRENT);
        }
        Account accountCreated = accountRepository.save(account);
        LOGGER.info("account created with id: {}",accountCreated.getAccountNo());
        return responseUtils.handleResponseInPayload(accountCreated,"Account created successfully",201, HttpStatus.CREATED);
    }

    @Override
    public Account getAccountDetailsByCustomerId(Long customerId) {
        LOGGER.info("Get Account By CustomerId: {}",customerId);
        Optional<Account> existAccount = accountRepository.findAccountByCustomerId(customerId);
        if(existAccount.isEmpty()){
            LOGGER.info("Account not found with id: {}",customerId);
            throw new ResourceNotFoundException("Account not found with id " + customerId);
        }
        return existAccount.get();
    }

    public void changeAccountStatus(Long customerId, Status status) {
        Account account = getAccountDetailsByCustomerId(customerId);
        switch (status){
            case DE_ACTIVE :
                account.setAccountStatus(Status.DE_ACTIVE);
                break;
            case CLOSE :
                account.setAccountStatus(Status.CLOSE);
                break;
            case PENDING :
                account.setAccountStatus(Status.PENDING);
                break;
            default:
                throw new InvalidInputException("Invalid type: status");
        }
       accountRepository.save(account);
    }

    @Override
    public ResponseEntity<ResponseJson> handleRequestChangeAccount(Long customerId) {
//        changeAccountStatus(Long customerId, Status status)
        return responseUtils.handleResponseInPayload(null,"Account closed successfully",200, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseJson> handleCloseAccount(Long customerId) {
        changeAccountStatus(customerId, Status.CLOSE);
        return responseUtils.handleResponseInPayload(null,"Account closed successfully",200, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseJson> handleActiveAccountStatus(Long customerId) {
        changeAccountStatus(customerId, Status.ACTIVE);
        return responseUtils.handleResponseInPayload(null,"Account closed successfully",200, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseJson> handleDeActiveAccountStatus(Long customerId) {
        changeAccountStatus(customerId, Status.DE_ACTIVE);
        return responseUtils.handleResponseInPayload(null,"Account De activated successfully",200, HttpStatus.OK);
    }

    @Override
    public Account getAccountById(Long id) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if(existingAccount.isEmpty()){
            throw new ResourceNotFoundException("Account not found with id " + id);
        }
        return existingAccount.get();
    }

    @Override
    public AccountStatusDTO handleGetAccountNoAndStatusByCustomerId(Long customerId) {
        LOGGER.info("Get Account Number By CustomerId: {}",customerId);
        return accountRepository.findAccountNoAndStatusByCustomerId(customerId);
    }

    @Override
    public ResponseEntity<ResponseJson> deleteAccount(Long id) {
        accountRepository.deleteById(id);
        return null;
    }


}
