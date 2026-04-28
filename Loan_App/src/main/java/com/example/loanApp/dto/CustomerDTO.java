package com.example.loanApp.dto;

import com.example.loanApp.enums.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public final class CustomerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
}


