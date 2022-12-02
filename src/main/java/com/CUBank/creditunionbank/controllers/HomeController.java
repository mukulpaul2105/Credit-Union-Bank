package com.CUBank.creditunionbank.controllers;

import com.CUBank.creditunionbank.constants.ErrorCode;
import com.CUBank.creditunionbank.dtos.*;
import com.CUBank.creditunionbank.exceptions.NoCoDAccountFoundException;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.models.requests.JwtAuthenticationReq;
import com.CUBank.creditunionbank.models.responses.ApiResponse;
import com.CUBank.creditunionbank.models.responses.ErrorCustom;
import com.CUBank.creditunionbank.models.responses.JwtAuthenticatedRes;
import com.CUBank.creditunionbank.services.IAdminService;
import com.CUBank.creditunionbank.services.ICodService;
import com.CUBank.creditunionbank.services.IMoneyMarketService;
import com.CUBank.creditunionbank.utils.JwtUtils;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping
public class HomeController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IMoneyMarketService moneyMarketService;

    @Autowired
    private ICodService codService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<ApiResponse<JwtAuthenticatedRes>> authenticate(
            @Validated @RequestBody final JwtAuthenticationReq jwtReq) {
        log.info("Received Authentication Request for Account Number: ", jwtReq.getAccountNumber());
        final UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(jwtReq.getAccountNumber(), jwtReq.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
        } catch(final DisabledException | BadCredentialsException | LockedException e) {
            log.info("Error Authenticating Account Number: ", jwtReq.getAccountNumber());
            if(e instanceof BadCredentialsException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(ErrorCustom.create(ErrorCode.INVALID_CREDENTIALS, "Please Enter valid Credentials")));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        final String accNumber = jwtReq.getAccountNumber();
        JWTUserDTO jwtUserDTO = new JWTUserDTO();
        if(accNumber.startsWith("11")) {
            final CoDDTO userDto;
            try {
                userDto = codService.getByAccountNumber(jwtReq.getAccountNumber());
            } catch (NoCoDAccountFoundException e) {
                log.error("COD account number not found: ", jwtReq.getAccountNumber());
                return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.COD_ACCOUNT_NUMBER_NOT_FOUND, "No Account found with Account Number: " + jwtReq.getAccountNumber())));
            }
            jwtUserDTO = jwtUserDTO.copyOf(MapperUtil.convertTo(userDto, JWTUserDTO.class));
        } else if(accNumber.startsWith("44") || accNumber.startsWith("77")) {
            final MoneyMarketDTO userDto;
            try {
                userDto = moneyMarketService.getByAccountNumber(jwtReq.getAccountNumber());
                jwtUserDTO = jwtUserDTO.copyOf(MapperUtil.convertTo(userDto, JWTUserDTO.class));
            } catch (NoMoneyMarketAccountFoundException e) {
                return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.MONEY_MARKET_ACCOUNT_NUMBER_NOT_FOUND, "No account found with account number: "+jwtReq.getAccountNumber())));
            }

        } else if(accNumber.startsWith("33")) {
            final AdminDTO userDto = adminService.getByAccountNumber(jwtReq.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("User with Username " + jwtReq.getAccountNumber() + " not found after Authentication"));
            jwtUserDTO = jwtUserDTO.copyOf(MapperUtil.convertTo(userDto, JWTUserDTO.class));
        } else {
            log.error("Invalid Credentials");
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorCustom.create(ErrorCode.INVALID_CREDENTIALS, "Please enter valid credentials")));
        }

        final JwtTokenDTO tokenDTO = jwtUtils.generateToken(jwtUserDTO);
        return ResponseEntity.ok(ApiResponse.success(MapperUtil.convertTo(tokenDTO, JwtAuthenticatedRes.class)));
    }
}
