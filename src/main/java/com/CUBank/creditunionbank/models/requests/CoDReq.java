package com.CUBank.creditunionbank.models.requests;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CoDReq {

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

    @NotNull(message = "Account Number cannot be null")
    @NotEmpty(message = "Account Number cannot be Empty")
    @Size(min = 10, max = 15, message = "Account Number can be with in 10 to 15 Digits")
    private String accountNumber;

    @NotNull(message = "Balance cannot be null")
    private Double balance;

    private String password;

    @NotNull(message = "Maturity Date cannot be null")
    private LocalDate maturityDate;
}
