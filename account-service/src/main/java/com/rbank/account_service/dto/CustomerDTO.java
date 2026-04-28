package com.rbank.account_service.dto;

import com.rbank.account_service.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDTO {
    @NotBlank(message = "Customer Id required")
    private Long customerId;
    @NotBlank(message = "Status required")
    private Status status;
}
