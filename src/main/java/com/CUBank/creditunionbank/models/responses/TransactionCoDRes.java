package com.CUBank.creditunionbank.models.responses;

import com.CUBank.creditunionbank.enums.TransactionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionCoDRes {

    private String accountNumber;
    private Double ReceivedAmount;
    private Double principalAmount;
    private LocalDateTime createdOn;
    private LocalDate openedOn;
    private TransactionStatus status;
    private String description;
}
