package com.rbank.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rbank.auth_service.enums.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class CustomerAuthDTO {
    private Long customerId;
    private String username;
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Set<RoleType> roles;

}
