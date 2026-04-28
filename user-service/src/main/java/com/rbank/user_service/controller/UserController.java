package com.rbank.user_service.controller;

import com.rbank.user_service.dto.CustomerAuthDTO;
import com.rbank.user_service.dto.CustomerDTO;
import com.rbank.user_service.dto.ResponseDTO;
import com.rbank.user_service.model.Customer;
import com.rbank.user_service.service.CustomerService;
import com.rbank.user_service.utils.ResponseUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final CustomerService customerService;
    @Autowired
    private ResponseUtils responseUtils;

    public UserController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO){
        return customerService.handleRegisterCustomer(customerDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
       Customer customer = customerService.getCustomerById(id);
       return ResponseEntity.status(200).body(customer);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminOnly() {
        return "Welcome, Admin!";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/user")
    public String userOnly() {
        return "Welcome, User!";
    }

    @GetMapping("/username")
    public CustomerAuthDTO getCustomerWithRoles(@RequestParam String username){
        return  customerService.handleGetCustomerWithRoles(username);
    }
}

