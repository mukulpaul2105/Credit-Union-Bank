package com.CUBank.creditunionbank.repositories;

import com.CUBank.creditunionbank.entitites.MoneyMarketEntity;
import com.CUBank.creditunionbank.enums.AccountType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoneyMarketRepo extends IDedRepo<MoneyMarketEntity> {

    Optional<MoneyMarketEntity> findByAccountNumber(final String accountNumber);
    List<MoneyMarketEntity> findByAccountType(final AccountType accountType);

    @Query(value = "SELECT MAX(m.accountNumber) FROM moneyMarket AS m WHERE m.accountType = ?1")
    public String findMaxAccountNum(AccountType accountType);
}
