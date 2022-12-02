package com.CUBank.creditunionbank.repositories;

import com.CUBank.creditunionbank.entitites.AccOpenEntity;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccOpenRepo extends IDedRepo<AccOpenEntity> {

    List<AccOpenEntity> findByAccountStatus(final AccountStatus status);

    List<AccOpenEntity> findByAccountTypeAndIdentityNumber(final AccountType accountType, final String identityNumber);
}
