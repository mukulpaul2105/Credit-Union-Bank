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
@Entity(name = EntityName.CERTIFICATE_OF_DEPOSIT)
@Table(name = TableName.CERTIFICATE_OF_DEPOSIT)
public class CoDEntity extends AccountEntity {

    @Column(name = "account_type", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "dob", columnDefinition = "DATE", nullable = false)
    private LocalDate dob;

    @Column(name = "account_number", columnDefinition = "VARCHAR(20)")
    private String accountNumber;

    @Column(name = "balance", columnDefinition = "DOUBLE(15, 2)")
    private Double balance;

    @Column(name = "password", columnDefinition = "VARCHAR(150)")
    private String password;

    @Column(name = "maturity_date", columnDefinition = "DATE")
    private LocalDate maturityDate;

    @Column(name = "interest_pa_in_percentage", columnDefinition =  "Decimal(5, 2)",nullable = false)
    private Float interestPa;

}
