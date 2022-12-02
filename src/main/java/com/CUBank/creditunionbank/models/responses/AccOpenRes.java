package com.CUBank.creditunionbank.models.responses;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AccOpenRes {

    private long id;
    private String accountNumber;
    private AccountType accountType;
    private IdentityType identityType;
    private String identityNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private Long contactNumber;
    private String email;
    private LocalDate dob;
    private LocalDateTime requestedOn;
    private LocalDateTime createdOn;
    private AccountStatus accountStatus;
    private LocalDate maturityDate;
    private Double amount;
}
