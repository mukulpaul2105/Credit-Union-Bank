package com.CUBank.creditunionbank.entitites;

import com.CUBank.creditunionbank.constants.EntityName;
import com.CUBank.creditunionbank.constants.TableName;
import com.CUBank.creditunionbank.enums.TransactionStatus;
import com.CUBank.creditunionbank.enums.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity(name = EntityName.TRANSACTION_MONEY_MARKET)
@Table(name = TableName.TRANSACTION_MONEY_MARKET)
public class TransactionMMEntity extends IDedEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_from", referencedColumnName = "account_number")
    private MoneyMarketEntity sender;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_to", referencedColumnName = "account_number")
    private MoneyMarketEntity receiver;

    @Column(name = "amount", columnDefinition = "DOUBLE(10,2)")
    private Double amount;

    @Column(name = "transaction_date_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "transaction_type", columnDefinition = "VARCHAR(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_status", columnDefinition = "VARCHAR(20)", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "description", columnDefinition = "VARCHAR(150)")
    private String description;

}
