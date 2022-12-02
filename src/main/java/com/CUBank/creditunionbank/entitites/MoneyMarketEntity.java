package com.CUBank.creditunionbank.entitites;

import com.CUBank.creditunionbank.constants.EntityName;
import com.CUBank.creditunionbank.constants.TableName;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = EntityName.MONEY_MARKET)
@Table(name = TableName.MONEY_MARKET)
public class MoneyMarketEntity extends AccountEntity {

    @Column(name = "account_type", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "dob", columnDefinition = "DATE", nullable = false)
    private LocalDate dob;

    @Column(name = "account_number", columnDefinition = "VARCHAR(20)", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "minimum_req_balance")
    private Double minReqBal;

    @Column(name = "password", columnDefinition = "VARCHAR(150)", nullable = false)
    private String password;
}
