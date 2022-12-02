package com.CUBank.creditunionbank.dtos;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdminDTO extends IDedDTO {

    private String firstName;
    private String lastName;
    private AccountType accountType;
    private AccountStatus status;
    private String accountNumber;
    private String password;
    private Integer accountOpened;
}
