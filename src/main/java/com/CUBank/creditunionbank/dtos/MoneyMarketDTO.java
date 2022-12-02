package com.CUBank.creditunionbank.dtos;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoneyMarketDTO extends AccountDTO {

    private AccountType accountType;
    private AccountStatus status;
    private LocalDate dob;
    private String accountNumber;
    private Double balance;
    private Double minReqBal;
    private String password;
}
