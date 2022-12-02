package com.CUBank.creditunionbank.models.responses;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import lombok.Data;

@Data
public class AdminRes {

    private String firstName;
    private String lastName;
    private AccountType accountType;
    private AccountStatus status;
    private String accountNumber;
    private Integer accountOpened;
}
