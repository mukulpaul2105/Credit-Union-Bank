package com.CUBank.creditunionbank.models.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class JwtAuthenticationReq {

    @NotNull(message = "Account Number cannot be null")
    @NotEmpty(message = "Account Number cannot be empty")
    @Size(min = 8, max = 15, message = "Account Number must be with in 8 to 15 characters")
    private String accountNumber;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, max = 20, message = "Password must be with in 6 to 20 characters")
    private String password;
}
