package com.CUBank.creditunionbank.dtos;

import com.CUBank.creditunionbank.enums.TransactionStatus;
import com.CUBank.creditunionbank.enums.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransactionMMDTO extends IDedDTO {

    private String transferFrom;
    private String transferTo;
    private Double amount;
    private LocalDateTime createdOn;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
}
