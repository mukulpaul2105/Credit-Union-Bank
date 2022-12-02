package com.CUBank.creditunionbank.models.responses;

import com.CUBank.creditunionbank.enums.TransactionStatus;
import com.CUBank.creditunionbank.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionMMRes {

    private String transferFrom;
    private String transferTo;;
    private Double amount;
    private LocalDateTime createdOn;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
}
