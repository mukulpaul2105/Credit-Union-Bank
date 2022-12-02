package com.CUBank.creditunionbank.models.responses;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MoneyMarketRes {

    private String firstName;
    private String lastName;
    private String middleName;
    private IdentityType identityType;
    private String identityNumber;
    private Long contactNumber;
    private String email;
    private AccountType accountType;
    private AccountStatus status;
    private LocalDate dob;
    private LocalDateTime createdOn;
    private String accountNum;
    private Double balance;
    private Double minReqBal;

}
