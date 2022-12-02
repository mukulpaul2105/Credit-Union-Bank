package com.CUBank.creditunionbank.services.impls;

import com.CUBank.creditunionbank.constants.TransactionDescription;
import com.CUBank.creditunionbank.dtos.TransactionCoDDTO;
import com.CUBank.creditunionbank.dtos.TransactionMMDTO;
import com.CUBank.creditunionbank.entitites.CoDEntity;
import com.CUBank.creditunionbank.entitites.MoneyMarketEntity;
import com.CUBank.creditunionbank.entitites.TransactionCoDEntity;
import com.CUBank.creditunionbank.entitites.TransactionMMEntity;
import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.enums.TransactionStatus;
import com.CUBank.creditunionbank.enums.TransactionType;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.exceptions.SameSenderAndReceiver;
import com.CUBank.creditunionbank.repositories.CoDRepo;
import com.CUBank.creditunionbank.repositories.MoneyMarketRepo;
import com.CUBank.creditunionbank.repositories.TransactionCoDRepo;
import com.CUBank.creditunionbank.repositories.TransactionMMRepo;
import com.CUBank.creditunionbank.services.ITransactionMMService;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class DefaultTransactionMMServiceImpl implements ITransactionMMService {

    @Autowired
    private MoneyMarketRepo moneyMarketRepo;

    @Autowired
    private CoDRepo coDRepo;

    @Autowired
    private TransactionCoDRepo tranCoDRepo;

    @Autowired
    private TransactionMMRepo transactionMMRepo;

    @Override
    @Transactional
    public Optional<TransactionMMDTO> withdraw(final String accNum, final Double amount) {
        log.info("Retrieving Account for amount withdraw: ");
        Optional<MoneyMarketEntity> mmEntity = moneyMarketRepo.findByAccountNumber(accNum);
        TransactionMMDTO tsDto;
        if(mmEntity.isPresent()) {
            MoneyMarketEntity entity = mmEntity.get();

            TransactionMMEntity tsEntity = new TransactionMMEntity();
            tsEntity.setSender(entity);
            tsEntity.setTransactionType(TransactionType.WITHDRAW);
            tsEntity.setCreatedOn(LocalDateTime.now());
            tsEntity.setAmount(amount);
            // Checking if the available balance is less than amount or not
            if(entity.getBalance() < amount) {
                tsEntity.setDescription(TransactionDescription.BALANCE_NOT_AVAILABLE);
                tsEntity.setStatus(TransactionStatus.FAIL);
                tsEntity.setAmount(amount);
                tsEntity = transactionMMRepo.save(tsEntity);
                tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                tsDto.setTransferFrom(accNum);
                log.info("Balance not available");
                return Optional.of(tsDto);
            }
            // This block is for Consumer type account
            if(entity.getAccountType().equals(AccountType.CONSUMER)) {
                // for consumer type account we have minimum require balance and here checking that
                if(entity.getBalance() - entity.getMinReqBal() <= amount) {
                    tsEntity.setDescription(TransactionDescription.MINIMUM_REQ_BAL);
                    tsEntity.setAmount(amount);
                    tsEntity.setStatus(TransactionStatus.FAIL);
                    tsEntity = transactionMMRepo.save(tsEntity);
                    tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                    tsDto.setTransferFrom(accNum);
                    log.info("Required minimum require balance");
                    return Optional.of(tsDto);
                } else {
                    Double balance = entity.getBalance() - amount;
                    entity.setBalance(balance);
                    entity = moneyMarketRepo.save(entity);
                    tsEntity.setStatus(TransactionStatus.SUCCESS);
                    tsEntity = transactionMMRepo.save(tsEntity);
                    tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                    tsDto.setTransferFrom(accNum);
                    log.info("amount: ", amount, " added to account number: ", accNum, " available balance: ", balance);
                    return Optional.of(tsDto);
                }

                //This is for Commercial Account
            } else {
                Double balance = entity.getBalance() - amount;
                entity.setBalance(balance);
                entity = moneyMarketRepo.save(entity);
                tsEntity.setStatus(TransactionStatus.SUCCESS);
                tsEntity = transactionMMRepo.save(tsEntity);
                tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                tsDto.setTransferFrom(accNum);
                log.info("amount: ", amount, " added to account number: ", accNum, " available balance: ", balance);
                return Optional.of(tsDto);
            }

        }
        log.info("Account not found with account number: ", accNum);
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<TransactionMMDTO> deposit(final String accNum, final Double amount) {
        log.info("Retrieving Account for amount deposit: ");
        Optional<MoneyMarketEntity> mmEntity = moneyMarketRepo.findByAccountNumber(accNum);
        if(mmEntity.isPresent()) {
            MoneyMarketEntity entity = mmEntity.get();
            Double balance = entity.getBalance() + amount;
            entity.setBalance(balance);
            entity = moneyMarketRepo.save(entity);
            TransactionMMEntity tsEntity = new TransactionMMEntity();
            tsEntity.setAmount(amount);
            tsEntity.setStatus(TransactionStatus.SUCCESS);
            tsEntity.setTransactionType(TransactionType.DEPOSIT);
            tsEntity.setCreatedOn(LocalDateTime.now());
            tsEntity.setReceiver(entity);
            tsEntity = transactionMMRepo.save(tsEntity);
            TransactionMMDTO dto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
            dto.setTransferTo(accNum);
            log.info("amount: ", amount, " added to account number: ", accNum, " available balance: ", balance);
            return Optional.of(dto);
        }
        log.info("Account not found with account number: ", accNum);
        return null;
    }

    @Override
    @Transactional
    public TransactionMMDTO  transfer(final String fromAcc, final String toAcc, final Double amount) throws NoMoneyMarketAccountFoundException {
        log.info("Received request for amount transfer");
        MoneyMarketEntity senderEntity = moneyMarketRepo.findByAccountNumber(fromAcc).orElseThrow(() -> new NoMoneyMarketAccountFoundException("No Account found with account number: " +  toAcc));
            MoneyMarketEntity receiverEn = moneyMarketRepo.findByAccountNumber(toAcc).orElseThrow(() -> new NoMoneyMarketAccountFoundException("No Account found with account number: " +  toAcc));

            TransactionMMDTO tsDto;
            TransactionMMEntity tsEntity = new TransactionMMEntity();
            tsEntity.setSender(senderEntity);
            tsEntity.setReceiver(receiverEn);
            tsEntity.setAmount(amount);
            tsEntity.setTransactionType(TransactionType.TRANSFER);
            tsEntity.setCreatedOn(LocalDateTime.now());
//            if(fromAcc.equals(toAcc)) {
//                tsEntity.setDescription(TransactionDescription.TRANSFER_TO_SAME_ACC);
//                tsEntity.setStatus(TransactionStatus.FAIL);
//                tsEntity = transactionMMRepo.save(tsEntity);
//                tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
//                tsDto.setTransferFrom(fromAcc);
//                tsDto.setTransferTo(toAcc);
//                log.info("Transaction between same account number cannot possible: ", fromAcc, " ", toAcc);
//                return tsDto;
//
//            }
            // Checking if the available balance is less than amount or not
            if (senderEntity.getBalance() < amount) {
                tsEntity.setDescription(TransactionDescription.BALANCE_NOT_AVAILABLE);
                tsEntity.setStatus(TransactionStatus.FAIL);
                tsEntity = transactionMMRepo.save(tsEntity);
                tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                tsDto.setTransferFrom(fromAcc);
                tsDto.setTransferTo(toAcc);
                log.info("Balance not available");
                return tsDto;
            }
            //Checking if the receiver account is valid or not
//            if(receiverOpt.isEmpty()) {
//                tsEntity.setDescription(TransactionDescription.RECEIVER_ACC_NOT_AVAILABLE);
//                tsEntity.setStatus(TransactionStatus.FAIL);
//                tsEntity = transactionMMRepo.save(tsEntity);
//                tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
//                tsDto.setTransferFrom(fromAcc);
//                tsDto.setTransferFrom(toAcc);
//                log.info("invalid receiver account: ", toAcc);
//                return tsDto;
//            }
//            MoneyMarketEntity receiverEn = receiverOpt;
            // This block is for Consumer type account
            if (senderEntity.getAccountType().equals(AccountType.CONSUMER)) {
                // for consumer type account we have minimum require balance and here checking that
                if (senderEntity.getBalance() - senderEntity.getMinReqBal() <= amount) {
                    tsEntity.setDescription(TransactionDescription.MINIMUM_REQ_BAL);
                    tsEntity.setStatus(TransactionStatus.FAIL);
                    tsEntity = transactionMMRepo.save(tsEntity);
                    tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                    tsDto.setTransferFrom(fromAcc);
                    tsDto.setTransferTo(toAcc);
                    log.info("Required minimum require balance");
                    return tsDto;
                } else {
                    Double balance = senderEntity.getBalance() - amount;
                    senderEntity.setBalance(balance);
                    senderEntity = moneyMarketRepo.save(senderEntity);
                    receiverEn.setBalance((receiverEn.getBalance() + amount));
                    receiverEn = moneyMarketRepo.save(receiverEn);
                    tsEntity.setStatus(TransactionStatus.SUCCESS);
                    tsEntity = transactionMMRepo.save(tsEntity);
                    tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                    tsDto.setTransferFrom(fromAcc);
                    tsDto.setTransferTo(toAcc);
                    log.info("amount: ", amount, " added to account number: ", toAcc, " available balance: ", balance);
                    return tsDto;
                }

                //This is for Commercial Account
            } else {
                Double balance = senderEntity.getBalance() - amount;
                senderEntity.setBalance(balance);
                senderEntity = moneyMarketRepo.save(senderEntity);
                receiverEn.setBalance((receiverEn.getBalance() + amount));
                receiverEn = moneyMarketRepo.save(receiverEn);
                tsEntity.setStatus(TransactionStatus.SUCCESS);
                tsEntity = transactionMMRepo.save(tsEntity);
                tsDto = MapperUtil.convertTo(tsEntity, TransactionMMDTO.class);
                tsDto.setTransferTo(toAcc);
                tsDto.setTransferFrom(fromAcc);
                log.info("amount: ", amount, " added to account number: ", toAcc, " available balance: ", balance);
                return tsDto;
            }
    }


    /*
    This method is for Certificate of Deposit withdrawal
    Here are some of the rules or logics for withdrawal
    1. If Customer withdraws after maturity date then he will get interest of 7.5% PA on his Principal amount
        as simple interest. and it will be calculated till it's maturity date not after that
    2. If Customer withdraws before the maturity date then he/she will to pay penalty of 10 % of principal amount
        So the customer will receive
     */
    @Override
    @Transactional
    public Optional<TransactionCoDDTO> codWithdraw(final String accNum) {
        log.info("Received request for Certificate of Deposit for Account Number: ", accNum);
        final Optional<CoDEntity> codEntOpt = coDRepo.findByAccountNumber(accNum);
        if(codEntOpt.isPresent() && codEntOpt.get().getStatus().equals(AccountStatus.ACTIVE)) {
            log.info("Found Account for Account Number: ", accNum, " -> ", codEntOpt.get().getAccountType());
            CoDEntity codEntity = codEntOpt.get();
            TransactionCoDEntity tsEntity = new TransactionCoDEntity();
            tsEntity.setAccountNum(accNum);
            if(codEntity.getMaturityDate().isBefore(LocalDate.now()) || codEntity.getMaturityDate().isEqual(LocalDate.now())) {
                Double totalAmount = totalPaymentAmount(codEntity, codEntity.getCreatedOn(), codEntity.getMaturityDate());
                tsEntity.setAmount(totalAmount);
                tsEntity.setStatus(TransactionStatus.SUCCESS);
                tsEntity.setDescription(TransactionDescription.COD_WITHDRAW_SUCCESS);
                tsEntity.setCreatedOn(LocalDateTime.now());
                tsEntity.setPAmount(codEntity.getBalance());
                tsEntity.setOpenedOn(codEntity.getCreatedOn().toLocalDate());
                tsEntity = tranCoDRepo.save(tsEntity);
                codEntity.setBalance(0.0);
                codEntity.setStatus(AccountStatus.SUSPENDED);
                codEntity = coDRepo.save(codEntity);
                log.info("Transaction Successful for account number: ", accNum);
                return Optional.of(MapperUtil.convertTo(tsEntity, TransactionCoDDTO.class));

            } else {
                Double totalAmount = totalPaymentAmount(codEntity, codEntity.getCreatedOn(), LocalDate.now());
                Double tAmount = totalAmount - codEntity.getBalance() * 0.1;
                tsEntity.setAmount(tAmount);
                tsEntity.setStatus(TransactionStatus.SUCCESS);
                tsEntity.setDescription(TransactionDescription.COD_WITHDRAW_BEFORE_MATURITY);
                tsEntity.setCreatedOn(LocalDateTime.now().plusYears(10));
                tsEntity.setPAmount(codEntity.getBalance());
                tsEntity.setOpenedOn(codEntity.getCreatedOn().toLocalDate());
                tsEntity = tranCoDRepo.save(tsEntity);
                codEntity.setBalance(0.0);
                codEntity.setStatus(AccountStatus.SUSPENDED);
                codEntity = coDRepo.save(codEntity);
                log.info("Transaction Successful for account number: ", accNum);
                return Optional.of(MapperUtil.convertTo(tsEntity, TransactionCoDDTO.class));
            }
        }
        log.info("Account not found with Account Number: ", accNum);
        return Optional.empty();
    }

    // Helper method to calculate Total payment amount for Certificate_of_Deposit account
    private Double totalPaymentAmount(final CoDEntity codEntity, LocalDateTime from, LocalDate to) {

        int years = to.getYear() - from.getYear();
        int months = to.getMonth().getValue() - from.getMonth().getValue();
        Double principal = codEntity.getBalance();
        Double interestPA = codEntity.getInterestPa().doubleValue();
        Double interest;
        if(months > 0) {
            interest = (principal * (0.01 * interestPA) * years) +
                    (principal * months * (0.01 * interestPA) / 12);
        } else {
            interest = (principal * (0.01 * interestPA) * years);
        }
        Double totalAmount = principal + interest;
        return totalAmount;
    }

}
