package com.rbank.auth_service.client;

import com.rbank.auth_service.dto.CustomerAuthDTO;
import com.rbank.auth_service.dto.ResponseDTO;
import com.rbank.auth_service.dto.CustomerRegistrationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service" ,path = "/users")
public interface UserServiceClient {
    @PostMapping("/register")
    ResponseEntity<ResponseDTO> createCustomer(@RequestBody CustomerRegistrationDTO customerRegistrationDTO);

    @GetMapping("/username")
    CustomerAuthDTO getCustomerWithRoles(@RequestParam String username);
}
