package com.CUBank.creditunionbank.entitites;

import com.CUBank.creditunionbank.constants.EntityName;
import com.CUBank.creditunionbank.constants.TableName;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = EntityName.ACCOUNT_OPENING)
@Table(name = TableName.ACCOUNT_OPENING)
public class AccOpenEntity extends IDedEntity {

    @Column(name = "account_type", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "first_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String lastName;

    @Column(name = "middle_name", columnDefinition = "VARCHAR(50)")
    private String middleName;

    @Column(name = "identity_type", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private IdentityType identityType;

    @Column(name = "identity_number", columnDefinition = "VARCHAR(50)", nullable = false)
    private String identityNumber;

    @Column(name = "contact_number", nullable = false)
    private Long contactNumber;

    @Column(name = "email", columnDefinition = "VARCHAR(50)")
    private String email;

    @Column(name = "dob", columnDefinition = "DATE", nullable = false)
    private LocalDate dob;

    @Column(name = "requested_on", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime requestedOn;

    @Column(name = "created_on", columnDefinition = "DATETIME")
    private LocalDateTime createdOn;

    @Column(name = "password", columnDefinition = "VARCHAR(150)", nullable = false)
    private String password;

    @Column(name = "request_status", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "maturity_date", columnDefinition = "DATE")
    private LocalDate maturityDate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "approved_by")
    private Long createdBy;

}
