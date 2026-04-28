package com.rbank.transaction_service.dto;


import com.rbank.transaction_service.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountDTO {
    @NotBlank(message = "Customer Id required")
    private Long customerId;
    @NotBlank(message = "Status required")
    private Status status;
}
