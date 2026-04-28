package com.rbank.user_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rbank.user_service.enums.RoleType;
import com.rbank.user_service.model.Customer;
import com.rbank.user_service.model.Role;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class CustomerAuthDTO {
    private Long customerId;
    private String username;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Set<RoleType> roles;

    public CustomerAuthDTO(Customer customer){
        this.customerId = customer.getCustomerId();
        this.username = customer.getUsername();
        this.password = customer.getPassword();
        this.roles = customer.getRoles().stream().map(Role::getRoleType).collect(Collectors.toSet());
    }
}
