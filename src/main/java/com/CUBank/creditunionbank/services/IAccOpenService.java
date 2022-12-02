package com.CUBank.creditunionbank.services;

import com.CUBank.creditunionbank.dtos.AccOpenDTO;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.IdentityType;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

public interface IAccOpenService {

    AccOpenDTO openingForm(final AccOpenDTO openingDTO);

    Optional<AccOpenDTO> getById(final Long id);

    Optional<AccOpenDTO> updateById(final Long id, final AccountStatus status) throws DataIntegrityViolationException;

    List<AccOpenDTO> getAllPendingRequests();

    List<AccOpenDTO> getAllApprovedRequests();

    List<AccOpenDTO> getAll();

    Boolean checkByAccTypeAndIdentityNumber(final AccountType accountType, final String identityNumber);
}
