package com.CUBank.creditunionbank.services;

import com.CUBank.creditunionbank.dtos.CoDDTO;
import com.CUBank.creditunionbank.exceptions.NoCoDAccountFoundException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

public interface ICodService {

    CoDDTO create(final CoDDTO codDTO);

    CoDDTO getByAccountNumber(final String accNumber) throws NoCoDAccountFoundException;

//    CoDDTO updateBalance(final String accNumber, final Double balance) throws NoCoDAccountFoundException;


    List<CoDDTO> getAll();

    Double getBalance(final String accNum) throws NoCoDAccountFoundException;
}
