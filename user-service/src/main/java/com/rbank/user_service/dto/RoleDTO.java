package com.rbank.user_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rbank.user_service.enums.RoleType;
import lombok.Data;

@Data
public class RoleDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private RoleType roleType;
}
