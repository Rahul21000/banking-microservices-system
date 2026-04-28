package com.rbank.account_service.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "transaction-service")
public interface TransactionClient {
}
