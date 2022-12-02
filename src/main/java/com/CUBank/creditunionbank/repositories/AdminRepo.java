package com.CUBank.creditunionbank.repositories;

import com.CUBank.creditunionbank.entitites.AdminEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends IDedRepo<AdminEntity> {

    Optional<AdminEntity> findByAccountNumber(final String accountNumber);


    @Query(value = "SELECT MAX(m.accountNumber) FROM admin AS m")
    public String findMaxAccountNum();
}
