package com.rbank.transaction_service.service;

import com.rbank.transaction_service.dto.AccountNoStatusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", url = "http://localhost:8083")
public interface AccountServiceClient {

    @GetMapping("getAccountNoAndStatus/{customerId}")
    AccountNoStatusDTO getAccountNoAndStatusByCustomerId(@PathVariable Long customerId);
}
