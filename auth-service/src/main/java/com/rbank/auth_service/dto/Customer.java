package com.rbank.auth_service.dto;

import com.rbank.auth_service.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class Customer {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String username;
    private String password;
    private Set<RoleType> roles;
}
