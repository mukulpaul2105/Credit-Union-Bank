package com.CUBank.creditunionbank.controllers;

import com.CUBank.creditunionbank.constants.ErrorCode;
import com.CUBank.creditunionbank.dtos.CoDDTO;
import com.CUBank.creditunionbank.dtos.MoneyMarketDTO;
import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.exceptions.NoCoDAccountFoundException;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.models.responses.ApiResponse;
import com.CUBank.creditunionbank.models.responses.CoDRes;
import com.CUBank.creditunionbank.models.responses.ErrorCustom;
import com.CUBank.creditunionbank.models.responses.MoneyMarketRes;
import com.CUBank.creditunionbank.services.ICodService;
import com.CUBank.creditunionbank.services.IMoneyMarketService;
import com.CUBank.creditunionbank.utils.LoggedInContext;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
Opening new account related all the APIs available here

        End-Point(for Money Market)                           Task                            Authority         Accessible By
 1. /moneyMarket/account                         --> Get Account Details                       private          CUSTOMER
 2. /moneyMarket/accounts/?accountNumber=?       --> Get Account Details by Account Number     private          ADMIN
 3. /moneyMarket/accounts/?accountType=?         --> Get list of Accounts by Account Type      Private          ADMIN
 4. /moneyMarket/account/balance                 --> Get Account Balance                       Private          CUSTOMER
 5. /moneyMarket/accounts/balance?{accountNumber} -> Get Account Balance by Account Number     Private          ADMIN
 6. /moneyMarket/accounts/                        --> Get the list of all Accounts              Private          ADMIN

       (for Certificate of deposit)
 7. /cod/account                                  --> Get Account Details                       Private            CUSTOMER
 8. /cod/accounts/?{accountNumber}                --> Get Account Details by Account Number     Private          ADMIN
 9. /cod/accounts                                 --> Get the list of all COC Accounts          Private          ADMIN

 */
@RestController
@Slf4j
public class AccountController {

    @Autowired
    private ICodService codService;
    @Autowired
    private IMoneyMarketService mmService;

    @RequestMapping(value = "/moneyMarket/account", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<MoneyMarketRes>> getAccount() {
//        @RequestParam("accountNumber") final String accNum  , params = {"accountNumber"}
        String accNum = LoggedInContext.getCurrentUser();
        log.info("Received request for Account Number: ", accNum);
        try {
            final MoneyMarketDTO dto = mmService.getByAccountNumber(accNum);
            log.info("Retrieved Account by Account Number: ", accNum, ":  ", dto.getFirstName());
            MoneyMarketRes mres = MapperUtil.convertTo(dto, MoneyMarketRes.class);
            mres.setAccountNum(dto.getAccountNumber());
            return ResponseEntity.ok(ApiResponse.success(mres));
        } catch (NoMoneyMarketAccountFoundException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_ACCOUNT_NUMBER_NOT_FOUND, "No active Money Market Account found with account number: " + accNum)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_UNKNOWN_ERROR, "Unknown error related to Money Market Account found with account number: " + accNum)));
        }
    }

    @RequestMapping(value = "/moneyMarket/accounts/" , params = {"accountNumber"}, method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<MoneyMarketRes>> getByAccountNum(
            @RequestParam("accountNumber") final String accNum ) {
        log.info("Received request for Account Number: ", accNum);
        try {
            final MoneyMarketDTO dto = mmService.getByAccountNumber(accNum);
            log.info("Retrieved Account by Account Number: ", accNum, ":  ", dto.getFirstName());
            MoneyMarketRes mres = MapperUtil.convertTo(dto, MoneyMarketRes.class);
            mres.setAccountNum(dto.getAccountNumber());
            return ResponseEntity.ok(ApiResponse.success(mres));
        } catch (NoMoneyMarketAccountFoundException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_ACCOUNT_NUMBER_NOT_FOUND, "No active Money Market Account found with account number: " + accNum)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_UNKNOWN_ERROR, "Unknown error related to Money Market Account found with account number: " + accNum)));
        }
    }

    @RequestMapping(value = "/moneyMarket/accounts/", params = {"accountType"}, method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<MoneyMarketRes>>> getByAccType(@RequestParam(value = "accountType") AccountType accType) {
        log.info("Received request for Account Type: ", accType);
        if(accType.equals(AccountType.CONSUMER) || accType.equals(AccountType.COMMERCIAL)) {
            final List<MoneyMarketDTO> dtos = mmService.getByAccountType(accType);
            final List<MoneyMarketRes> mmRes = new ArrayList<>();
            dtos.forEach((e) -> {
                MoneyMarketRes mdto = MapperUtil.convertTo(e, MoneyMarketRes.class);
                mdto.setAccountNum(e.getAccountNumber());
                mmRes.add(mdto);
            });
            return ResponseEntity.ok().body(ApiResponse.success(mmRes));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.INVALID_ACCOUNT_TYPE, "Invalid Account type chose either CONSUMER OR COMMERCIAL")));
        }
    }


    @RequestMapping(value = "/moneyMarket/accounts/", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<MoneyMarketRes>>> getAll() {
        log.info("Received request for Accounts");
        final List<MoneyMarketDTO> dtos = mmService.getAll();
        final List<MoneyMarketRes> mmRes = new ArrayList<>();
        dtos.forEach((e) -> {
            MoneyMarketRes mdto = MapperUtil.convertTo(e, MoneyMarketRes.class);
            mdto.setAccountNum(e.getAccountNumber());
            mmRes.add(mdto);
        });
        return ResponseEntity.ok().body(ApiResponse.success(mmRes));
    }


    @RequestMapping(value = "/moneyMarket/account/balance", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<Double>> getBalance() {
        final String accNum = LoggedInContext.getCurrentUser();
        log.info("Received request for Account Number: ", accNum);
        try {
            final Double balance = mmService.getBalance(accNum);
            log.info("Retrieved balance by Account Number: ", accNum, ":  ", balance);
            return ResponseEntity.ok(ApiResponse.success(balance));
        } catch (NoMoneyMarketAccountFoundException e) {
            log.error("Account not found with Account Number: ", accNum);
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_ACCOUNT_NUMBER_NOT_FOUND, "No Account found with account Number: "+ accNum)));
        }
    }

    @RequestMapping(value = "/moneyMarket/accounts/balance/",  params = {"accountNumber"}, method = RequestMethod.GET)

    public ResponseEntity<ApiResponse<Double>> getBalanceByAccNum(
            @RequestParam(value = "accountNumber") final String accNum) throws NoMoneyMarketAccountFoundException {
        log.info("Received request for Account Number: ", accNum);
        try {
            final Double balance = mmService.getBalance(accNum);
            log.info("Retrieved balance by Account Number: ", accNum, ":  ", balance);
            return ResponseEntity.ok(ApiResponse.success(balance));
        } catch (NoMoneyMarketAccountFoundException e) {
            log.error("Account not found with Account Number: ", accNum);
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_ACCOUNT_NUMBER_NOT_FOUND, "No Account found with account Number: "+ accNum)));
        }
    }




    // For Certificate Of Diposit
    @RequestMapping(value = "/cod/account", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<CoDRes>> getCodAccount() {
        String accNum = LoggedInContext.getCurrentUser();
        log.info("Received request for Account Number: ", accNum);
        try {
            final CoDDTO dto = codService.getByAccountNumber(accNum);
            log.info("Retrieved Account by Account Number: ", accNum, ":  ", dto.getFirstName());
            CoDRes mres = MapperUtil.convertTo(dto, CoDRes.class);
            mres.setAccountNumber(dto.getAccountNumber());
            return ResponseEntity.ok(ApiResponse.success(mres));
        } catch (NoCoDAccountFoundException e) {
            log.error("Account number not found: ", accNum);
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.COD_ACCOUNT_NUMBER_NOT_FOUND, "No Account found with account Number: "+ accNum)));
        }

    }

    @RequestMapping(value = "/cod/accounts/", params = {"accountNumber"}, method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<CoDRes>> getByAccNumber(
            @RequestParam("accountNumber") final String accNum) {
        log.info("Received request for Account Number: ", accNum);
        try {
            final CoDDTO dto = codService.getByAccountNumber(accNum);
            log.info("Retrieved Account by Account Number: ", accNum, ":  ", dto.getFirstName());
            CoDRes mres = MapperUtil.convertTo(dto, CoDRes.class);
            mres.setAccountNumber(dto.getAccountNumber());
            return ResponseEntity.ok(ApiResponse.success(mres));
        } catch (NoCoDAccountFoundException e) {
            log.error("Account number not found: ", accNum);
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.COD_ACCOUNT_NUMBER_NOT_FOUND, "No Account found with account Number: "+ accNum)));
        }
    }

    @RequestMapping(value = "/cod/accounts", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<CoDRes>>> getAllAcc() {
        log.info("Received request for Accounts");
        final List<CoDDTO> dtos = codService.getAll();
        final List<CoDRes> mmRes = new ArrayList<>();
        dtos.forEach((e) -> {
            CoDRes mdto = MapperUtil.convertTo(e, CoDRes.class);
            mdto.setAccountNumber(e.getAccountNumber());
            mmRes.add(mdto);
        });
        return ResponseEntity.ok().body(ApiResponse.success(mmRes));
    }

}
