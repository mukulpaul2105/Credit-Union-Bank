package com.CUBank.creditunionbank.services;

import com.CUBank.creditunionbank.dtos.*;
import com.CUBank.creditunionbank.exceptions.NoCoDAccountFoundException;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.utils.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserPrincipalService implements UserDetailsService {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private ICodService codService;

    @Autowired
    private IMoneyMarketService moneyMarketService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith("11")) {
            log.info("Finding COD account by account number: ", username);
            try {
                final CoDDTO coDDTO = codService.getByAccountNumber(username);
                JWTUserDTO userDTO = MapperUtil.convertTo(coDDTO, JWTUserDTO.class);
                return new UserPrincipalDTO(userDTO);
            } catch (NoCoDAccountFoundException e) {
                log.error("Account not found while Loading user by account number in UserPrincipalService");
                throw new UsernameNotFoundException(username);
            }

        } else if(username.startsWith("33")) {
            log.info("Finding Admin account by account number: ", username);
            final AdminDTO adminDTO = adminService.getByAccountNumber(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User with the username " + username + " not found in User Principal Service"));
            JWTUserDTO userDTO = MapperUtil.convertTo(adminDTO, JWTUserDTO.class);
            return new UserPrincipalDTO(userDTO);

        } else if(username.startsWith("44") || username.startsWith("77")) {
            log.info("Finding Money Market account by account number: ", username);
            try {
                final MoneyMarketDTO mmDto = moneyMarketService.getByAccountNumber(username);

                JWTUserDTO userDTO = MapperUtil.convertTo(mmDto, JWTUserDTO.class);
                return new UserPrincipalDTO(userDTO);
            } catch (NoMoneyMarketAccountFoundException e) {
                log.error("Account not found while Loading user by account number in UserPrincipalService");
                throw new UsernameNotFoundException(username);
            }
        }
        throw  new UsernameNotFoundException("User not found !!");
    }
}
