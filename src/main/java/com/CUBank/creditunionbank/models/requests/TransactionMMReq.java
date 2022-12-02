package com.CUBank.creditunionbank.models.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TransactionMMReq {

    @NotNull(message = "From Account cannot be null")
    @NotEmpty(message = "From Account cannot be empty")
    @Size(min = 10, max = 15, message = "Account Number must be with in 10 to 15 digits")
    private String transferFrom;

    @NotEmpty(message = "To Account cannot be Empty")
    private String transferTo;

    @NotNull(message = "Amount cannot be null")
    @Size(min = 1, max = 1000000, message = "Amount must be with in 1 to 1000000")
    private Double amount;

}
