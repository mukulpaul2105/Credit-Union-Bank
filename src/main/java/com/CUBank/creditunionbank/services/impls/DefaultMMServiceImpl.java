package com.CUBank.creditunionbank.services.impls;

import com.CUBank.creditunionbank.dtos.MoneyMarketDTO;
import com.CUBank.creditunionbank.entitites.MoneyMarketEntity;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.repositories.MoneyMarketRepo;
import com.CUBank.creditunionbank.services.IMoneyMarketService;
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
public class DefaultMMServiceImpl implements IMoneyMarketService {

    @Autowired
    private MoneyMarketRepo mmRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MoneyMarketDTO create(final MoneyMarketDTO mmDTO) {
        log.info("Creating new Account: ", mmDTO.getAccountType());
        MoneyMarketEntity mmEntity = MapperUtil.convertTo(mmDTO, MoneyMarketEntity.class);

        if(mmEntity.getAccountType().equals(AccountType.CONSUMER)) {
            Long maxAccNum = Long.parseLong(mmRepo.findMaxAccountNum(mmEntity.getAccountType()));
            maxAccNum += 2L;
            mmEntity.setAccountNumber(maxAccNum.toString());
            mmEntity.setMinReqBal(1000.00);
        } else if(mmEntity.getAccountType().equals(AccountType.COMMERCIAL)) {
            Long maxAccNum = Long.parseLong(mmRepo.findMaxAccountNum(mmEntity.getAccountType()));
            maxAccNum += 2L;
            mmEntity.setAccountNumber(maxAccNum.toString());
            mmEntity.setMinReqBal(00.00);
        } else {
            return null;
        }
        mmEntity.setBalance(00.00);
        mmEntity.setCreatedOn(LocalDateTime.now());
        mmEntity.setStatus(AccountStatus.ACTIVE);
        mmEntity = mmRepo.save(mmEntity);
        mmEntity.setPassword(passwordEncoder.encode(mmDTO.getPassword()));
        log.info("Created new Account: ", mmEntity.getAccountNumber());
        final MoneyMarketDTO mdto = MapperUtil.convertTo(mmEntity, MoneyMarketDTO.class);
        mdto.setAccountNumber(mmEntity.getAccountNumber());
        return mdto;
    }

    @Override
    public MoneyMarketDTO getByAccountNumber(String accNumber) throws NoMoneyMarketAccountFoundException {
        log.info("Retrieving Account by Account Number: ", accNumber);
        MoneyMarketEntity mmEntity = mmRepo.findByAccountNumber(accNumber)
                .orElseThrow(() -> new NoMoneyMarketAccountFoundException("No Account found with Account NUmber: " + accNumber));

        log.info("Retrieved Account: ", mmEntity.getAccountNumber());
        final MoneyMarketDTO mmdto = MapperUtil.convertTo(mmEntity, MoneyMarketDTO.class);
        mmdto.setAccountNumber(mmEntity.getAccountNumber());
        return mmdto;
    }

    @Override
    @Transactional
    public Optional<MoneyMarketDTO> updateBalance(String accNumber, Double balance) throws DataIntegrityViolationException {
        return Optional.empty();
    }

    @Override
    public List<MoneyMarketDTO> getAll() {
        log.info("Retrieving all Account ");
        final List<MoneyMarketEntity> mmEntity = mmRepo.findAll();
        final List<MoneyMarketDTO> dtos = new ArrayList<>();
        mmEntity.forEach((e) -> {
            MoneyMarketDTO mdto = MapperUtil.convertTo(e, MoneyMarketDTO.class);
            mdto.setAccountNumber(e.getAccountNumber());
            dtos.add(mdto);
        });
        return dtos;
    }

    @Override
    public List<MoneyMarketDTO> getByAccountType(AccountType accType) {
        log.info("Retrieving all Account by Account Type: ", accType);
        List<MoneyMarketEntity> mmEntity = mmRepo.findByAccountType(accType);
        List<MoneyMarketDTO> dtos = new ArrayList<>();
        mmEntity.forEach((e) -> {
            MoneyMarketDTO mdto = MapperUtil.convertTo(e, MoneyMarketDTO.class);
            mdto.setAccountNumber(e.getAccountNumber());
            dtos.add(mdto);
        });
        return dtos;
    }

    @Override
    public Double getBalance(String accNum) throws NoMoneyMarketAccountFoundException {
        log.info("Retrieving Account by Account Number: ", accNum);
        MoneyMarketEntity mmEntity = mmRepo.findByAccountNumber(accNum)
                .orElseThrow(() -> new NoMoneyMarketAccountFoundException("No Account found with Account NUmber: " + accNum));
        return mmEntity.getBalance();
    }
}
