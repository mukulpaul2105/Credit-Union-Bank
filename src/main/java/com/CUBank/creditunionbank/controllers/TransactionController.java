package com.CUBank.creditunionbank.controllers;

import com.CUBank.creditunionbank.constants.ErrorCode;
import com.CUBank.creditunionbank.constants.TransactionDescription;
import com.CUBank.creditunionbank.dtos.TransactionCoDDTO;
import com.CUBank.creditunionbank.dtos.TransactionMMDTO;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.models.responses.ApiResponse;
import com.CUBank.creditunionbank.models.responses.ErrorCustom;
import com.CUBank.creditunionbank.models.responses.TransactionCoDRes;
import com.CUBank.creditunionbank.models.responses.TransactionMMRes;
import com.CUBank.creditunionbank.services.ITransactionMMService;
import com.CUBank.creditunionbank.utils.LoggedInContext;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
/*
Transaction related all the REST APIs available here

        End-Points (for Money Market)                      Task                 Authority       Accessible By
 1. /transaction/withdraw?amount=?                   --> Withdraw amount          private          CUSTOMER
 2. /transaction/deposit?amount=?                    --> Deposit amount           Private          CUSTOMER
 3. /transaction/transfer?accountNumber=?&amount=?   --> Transfer amount          Private          CUSTOMER

        for(Certificate of Deposit)
 4. /cod/withdraw                                     --> Withdraw amount          private          CUSTOMER

 */
@RestController
@Slf4j
public class TransactionController {

    @Autowired
    private ITransactionMMService transactionMMService;

    @RequestMapping(value = "/transaction/withdraw", params = { "amount"}, method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<TransactionMMRes>> withdraw(
            @RequestParam(value = "amount") final Double amount) {
        final String accNum = LoggedInContext.getCurrentUser();
        log.info("Received Withdrawal Request for Account Number: ", accNum, " amount: ", amount);
        Optional<TransactionMMDTO> tsOptional = transactionMMService.withdraw(accNum, amount);
        if(tsOptional.isPresent()) {
            log.info("Transaction Successful: ");
            return ResponseEntity.ok(ApiResponse.success(MapperUtil.convertTo(tsOptional.get(), TransactionMMRes.class)));
        }
        log.info("Transaction Fail: ");
        return ResponseEntity.badRequest().body(ApiResponse.success(null));
    }

    @RequestMapping(value = "/transaction/deposit", params = {"amount"}, method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<TransactionMMRes>> deposit(
//            @RequestParam(value = "accountNumber") final String accNum,      "accountNumber",
            @RequestParam(value = "amount") final Double amount) {
        final String accNum = LoggedInContext.getCurrentUser();
        log.info("Received Deposit Request for Account Number: ", accNum, " amount: ", amount);
        Optional<TransactionMMDTO> tsOptional = transactionMMService.deposit(accNum, amount);
        if(tsOptional.isPresent()) {
            log.info("Transaction Successful: ");
            return ResponseEntity.ok(ApiResponse.success(MapperUtil.convertTo(tsOptional.get(), TransactionMMRes.class)));
        }
        log.info("Transaction Fail: ");
        return ResponseEntity.badRequest().body(ApiResponse.success(null));
    }

    @RequestMapping(value = "/transaction/transfer", params = {"accountNumber", "amount"}, method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<TransactionMMRes>> transfer(
//            @RequestParam(value = "accountNumber1") final String fromAcc,      "accountNumber1",
            @RequestParam(value = "accountNumber") final String toAcc,
            @RequestParam(value = "amount") final Double amount) {
        final String fromAcc = LoggedInContext.getCurrentUser();
        log.info("Received Transfer Request for Account Number: ", toAcc, " amount: ", amount);
        try {
            TransactionMMDTO tsDto = transactionMMService.transfer(fromAcc, toAcc, amount);
            if(tsDto.getDescription() == null) {
                log.info("Transaction Successful: ");
                return ResponseEntity.ok(ApiResponse.success(MapperUtil.convertTo(tsDto, TransactionMMRes.class)));
            } else if(tsDto.getDescription().equals(TransactionDescription.MINIMUM_REQ_BAL)) {
                log.error("Would not left minimum require balance");
                return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.MIN_BALANCE_REQUIREMENT, "Would not left minimum require balance")));
            }
            return ResponseEntity.ok(ApiResponse.success(MapperUtil.convertTo(tsDto, TransactionMMRes.class)));

        } catch (NoMoneyMarketAccountFoundException e) {
            log.error("No Account found with Account Number: ", toAcc);
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_ACCOUNT_NUMBER_NOT_FOUND, "Account not found with Account Number: " + toAcc)));
        }
    }

    @RequestMapping(value = "/transaction/cod/withdraw", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<TransactionCoDRes>> codWithdraw() {
//        @RequestParam(value = "accountNumber") final String accNum         params = {"accountNumber"},
        final String accNum = LoggedInContext.getCurrentUser();
        log.info("Received request for Certificate of Deposit Withdrawal for Account Number: ", accNum);
        Optional<TransactionCoDDTO> codDtoOpt = transactionMMService.codWithdraw(accNum);
        if(codDtoOpt.isPresent()) {
            log.info("Transaction Successful for Account Number: ", accNum);
            TransactionCoDRes tsRes = MapperUtil.convertTo(codDtoOpt, TransactionCoDRes.class);
            tsRes.setReceivedAmount(codDtoOpt.get().getAmount());
            tsRes.setPrincipalAmount(codDtoOpt.get().getPAmount());
            return ResponseEntity.ok(ApiResponse.success(tsRes));
        }
        return ResponseEntity.badRequest().body(ApiResponse.success(null));
    }

}
