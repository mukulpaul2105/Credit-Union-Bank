package com.CUBank.creditunionbank.models.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
public class TransactionCoDReq {

    @NotNull(message = "Account Number cannot be null")
    @NotEmpty(message = "Account Number cannot be empty")
    @Size(min = 10, max = 15, message = "Account Number must be with in 10 to 15 digits")
    private String accountNumber;
}
