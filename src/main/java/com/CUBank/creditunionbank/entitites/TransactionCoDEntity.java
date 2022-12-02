package com.CUBank.creditunionbank.entitites;

import com.CUBank.creditunionbank.constants.EntityName;
import com.CUBank.creditunionbank.constants.TableName;
import com.CUBank.creditunionbank.enums.TransactionStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = EntityName.TRANSACTION_COD)
@Table(name = TableName.TRANSACTION_COD)
public class TransactionCoDEntity extends IDedEntity{

    @Column(name = "account_number")
    private String accountNum;

    //Amount with interest
    @Column(name = "amount", columnDefinition = "DOUBLE(15,2)")
    private Double amount;

    // Principal Amount
    @Column(name = "principal_amount", columnDefinition = "DOUBLE(15,2)")
    private Double pAmount;

    @Column(name = "transaction_date_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "created_on", columnDefinition = "DATE", nullable = false)
    private LocalDate openedOn;

    @Column(name = "transaction_status", columnDefinition = "VARCHAR(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "description", columnDefinition = "VARCHAR(150)")
    private String description;
}
