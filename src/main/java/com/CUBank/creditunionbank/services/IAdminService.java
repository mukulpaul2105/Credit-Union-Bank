package com.CUBank.creditunionbank.services;

import com.CUBank.creditunionbank.dtos.AdminDTO;

import java.util.List;
import java.util.Optional;

public interface IAdminService {

    AdminDTO create(final AdminDTO adminDTO);
    Optional<AdminDTO> getByAccountNumber(final String accountNumber);
    List<AdminDTO> getAll();


}
