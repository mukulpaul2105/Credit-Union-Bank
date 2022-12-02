package com.CUBank.creditunionbank.repositories;

import com.CUBank.creditunionbank.entitites.CoDEntity;
import com.CUBank.creditunionbank.enums.AccountType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoDRepo extends IDedRepo<CoDEntity> {

    Optional<CoDEntity> findByAccountNumber(final String accountNumber);
//    List<CoDEntity> findByAccountType(final AccountType accountType);

    @Query(value = "SELECT MAX(m.accountNumber) FROM certificateOfDeposit AS m")
    public String findMaxAccountNum();
}
