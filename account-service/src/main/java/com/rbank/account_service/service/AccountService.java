package com.rbank.account_service.service;

import com.rbank.account_service.dto.AccountStatusDTO;
import com.rbank.account_service.dto.ResponseJson;
import com.rbank.account_service.model.Account;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<ResponseJson> handleCreateAccount(AccountStatusDTO requestDto);

    Account getAccountDetailsByCustomerId(Long customerId);

    ResponseEntity<ResponseJson> handleRequestChangeAccount(Long customerId);

    ResponseEntity<ResponseJson> handleCloseAccount(Long customerID);

    ResponseEntity<ResponseJson> handleActiveAccountStatus(Long customerId);

    ResponseEntity<ResponseJson> handleDeActiveAccountStatus(Long customerId);

    Account getAccountById(Long id);

    AccountStatusDTO handleGetAccountNoAndStatusByCustomerId(Long customerId);

    ResponseEntity<ResponseJson> deleteAccount(Long id);

}
