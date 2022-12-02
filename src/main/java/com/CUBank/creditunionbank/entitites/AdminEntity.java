package com.CUBank.creditunionbank.entitites;

import com.CUBank.creditunionbank.constants.EntityName;
import com.CUBank.creditunionbank.constants.TableName;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = EntityName.ADMIN)
@Table(name = TableName.ADMIN)
public class AdminEntity extends IDedEntity {

    @Column(name = "first_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(100)")
    private String lastName;

    @Column(name = "role", columnDefinition = "VARCHAR(30)", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "status", columnDefinition = "VARCHAR(30)", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "username", nullable = false, columnDefinition = "VARCHAR(20)", unique = true)
    private String accountNumber;

    @Column(name = "password", columnDefinition = "VARCHAR(150)",nullable = false)
    private String password;

    @Column(name = "account_opened", columnDefinition = "INT")
    private Integer accountOpened;
}
