package com.CUBank.creditunionbank.models.requests;

import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class AccOpenReq {

    @NotNull(message = "Account Type cannot be null")
    private AccountType accountType;

    @NotNull(message = "Identity Type cannot be null")
    private IdentityType identityType;

    @NotNull(message = "Identity Number cannot be null")
    @NotEmpty(message = "Identity Number cannot be null")
    @Size(min = 3, max = 50, message = "Identity Number should be with in 3 to 50 characters")
    private String identityNumber;

    @NotNull(message = "First Name cannot be null")
    @NotEmpty(message = "First Name cannot be null")
    @Size(min = 3, max = 50, message = "First Name should be with in 3 to 50 characters")
    private String firstName;

    @NotNull(message = "Last Name cannot be null")
    @NotEmpty(message = "Last Name cannot be null")
    @Size(min = 3, max = 50, message = "Last Name should be with in 3 to 50 characters")
    private String lastName;

    @Size(min = 3, max = 50, message = "Middle Name should be with in 3 to 50 characters")
    private String middleName;

    @NotNull(message = "Contact Number cannot be null")
    private Long contactNumber;


    @Size(min = 6, max = 50, message = "Email should be with in 6 to 50 characters")
    private String email;

    @NotNull(message = "Date of Birth cannot be null")
    private LocalDate dob;

    @NotNull(message = "Password cannot be null")
    @NotEmpty(message = "Password cannot be null")
    @Size(min = 8, max = 150, message = "Password should be with in 8 to 150 characters")
    private String password;

    private LocalDate maturityDate;
    private Double amount;
}
