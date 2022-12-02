package com.CUBank.creditunionbank.services.impls;

import com.CUBank.creditunionbank.dtos.CoDDTO;
import com.CUBank.creditunionbank.entitites.CoDEntity;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.exceptions.NoCoDAccountFoundException;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.repositories.CoDRepo;
import com.CUBank.creditunionbank.services.ICodService;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class DefaultICoDServiceImpl implements ICodService {
    
    @Autowired
    private CoDRepo coDRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional
    public CoDDTO create(CoDDTO codDTO) {
        log.info("Creating new Account: ", codDTO.getAccountType());
        CoDEntity codEntity = MapperUtil.convertTo(codDTO, CoDEntity.class);

        Long maxAccNum = Long.parseLong(coDRepo.findMaxAccountNum());
        maxAccNum += 2L;
        codEntity.setAccountNumber(maxAccNum.toString());
        //        codEntity.setAccountNumber("11100000001");
        codEntity.setInterestPa(7.5F);
        codEntity.setPassword(passwordEncoder.encode(codDTO.getPassword()));
        codEntity.setBalance(codDTO.getBalance());
        codEntity.setCreatedOn(LocalDateTime.now());
        codEntity.setStatus(AccountStatus.ACTIVE);
        codEntity = coDRepo.save(codEntity);
        codEntity.setMaturityDate(codDTO.getMaturityDate());
        log.info("Created new Account: ", codEntity.getAccountNumber());
        final CoDDTO mdto = MapperUtil.convertTo(codEntity, CoDDTO.class);
        mdto.setAccountNumber(codEntity.getAccountNumber());
        return mdto;
    }

    @Override
    public CoDDTO getByAccountNumber(String accNumber) throws NoCoDAccountFoundException {
        log.info("Retrieving Account by Account Number: ", accNumber);
        CoDEntity codEntity = coDRepo.findByAccountNumber(accNumber).orElseThrow(()-> new NoCoDAccountFoundException("No COD Account found with account number: "+ accNumber));
        log.info("Retrieved Account: ", codEntity.getAccountNumber());
        final CoDDTO mmdto = MapperUtil.convertTo(codEntity, CoDDTO.class);
        mmdto.setAccountNumber(codEntity.getAccountNumber());
        return mmdto;
    }


    @Override
    public List<CoDDTO> getAll() {
        log.info("Retrieving all Account ");
        final List<CoDEntity> mmEntity = coDRepo.findAll();
        final List<CoDDTO> dtos = new ArrayList<>();
        mmEntity.forEach((e) -> {
            CoDDTO mdto = MapperUtil.convertTo(e, CoDDTO.class);
            mdto.setAccountNumber(e.getAccountNumber());
            dtos.add(mdto);
        });
        return dtos;
    }

    @Override
    public Double getBalance(String accNumber) throws NoCoDAccountFoundException {
        log.info("Retrieving Account by Account Number: ", accNumber);
        CoDEntity codEntity = coDRepo.findByAccountNumber(accNumber).orElseThrow(()-> new NoCoDAccountFoundException("No COD Account found with account number: "+ accNumber));
        log.info("Retrieved Account: ", codEntity.getAccountNumber());
        final Double balance = codEntity.getBalance();
        return balance;
    }
}
