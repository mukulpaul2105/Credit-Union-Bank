package com.CUBank.creditunionbank.services;

import com.CUBank.creditunionbank.dtos.TransactionCoDDTO;
import com.CUBank.creditunionbank.dtos.TransactionMMDTO;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;

import java.util.Optional;

public interface ITransactionMMService {
    Optional<TransactionMMDTO> withdraw(final String accNum, final Double amount);
    Optional<TransactionMMDTO>  deposit(final String accNum, final Double amount);
    TransactionMMDTO transfer(final String fromAcc, final String toAcc, final Double amount) throws NoMoneyMarketAccountFoundException;


    Optional<TransactionCoDDTO> codWithdraw(final String accNum);
}
