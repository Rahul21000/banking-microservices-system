package com.rbank.user_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rbank.user_service.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public final class CustomerDTO {
    @NotBlank(message = "firstName is required")
    private String firstName;
    private String lastName;
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "mobile is required")
    private String mobile;
    @NotBlank(message = "username is required")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Set<RoleType> roles;
}


