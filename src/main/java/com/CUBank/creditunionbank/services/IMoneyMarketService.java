package com.CUBank.creditunionbank.services;

import com.CUBank.creditunionbank.dtos.MoneyMarketDTO;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

public interface IMoneyMarketService {

    MoneyMarketDTO create(final MoneyMarketDTO mmDTO);

    MoneyMarketDTO getByAccountNumber(final String accNumber) throws NoMoneyMarketAccountFoundException;

    Optional<MoneyMarketDTO> updateBalance(final String accNumber, final Double balance) throws DataIntegrityViolationException;

    List<MoneyMarketDTO> getAll();

    List<MoneyMarketDTO> getByAccountType(final AccountType accType);

    Double getBalance(final String accNum) throws NoMoneyMarketAccountFoundException;
}
