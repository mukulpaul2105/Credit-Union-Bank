package com.CUBank.creditunionbank.dtos;

import com.CUBank.creditunionbank.enums.TransactionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionCoDDTO {

    private String accountNumber;
    private Double amount;
    private Double pAmount;
    private LocalDateTime createdOn;
    private LocalDate openedOn;
    private TransactionStatus status;
    private String description;
}
