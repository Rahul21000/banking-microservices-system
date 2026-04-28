package com.rbank.user_service.dto;

import com.rbank.user_service.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountDTO {
    @NotNull(message = "Customer Id required")
    private Long customerId;
    @NotNull(message = "Status required")
    private StatusType statusType;
}
