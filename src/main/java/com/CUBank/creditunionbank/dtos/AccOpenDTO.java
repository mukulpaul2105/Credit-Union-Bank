package com.CUBank.creditunionbank.dtos;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccOpenDTO extends IDedDTO {

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
    private String password;
    private AccountStatus accountStatus;
    private LocalDate maturityDate;
    private Double amount;
    private Long createdBy;
}
