package com.mongodb.api.hrms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull(message = "username must not be null")
    private String username;

    @NotNull(message = "password must not be null")
    private String password;
}
