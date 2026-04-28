package com.rbank.account_service.service;

import com.rbank.account_service.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {
//    @GetMapping("/api/users/{id}")
//    CustomerDTO getUserById(@PathVariable Long id);
}
