package com.CUBank.creditunionbank.dtos;

import com.CUBank.creditunionbank.enums.AccountStatus;
import com.CUBank.creditunionbank.enums.AccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class JWTUserDTO extends IDedDTO {

    private String firstName;
    private String lastName;
    private AccountType accountType;
    private AccountStatus status;
    private String accountNumber;
    private String password;

    public JWTUserDTO copyOf(JWTUserDTO obj) {
        JWTUserDTO jwtUserDTO = new JWTUserDTO();
        jwtUserDTO.setAccountNumber(obj.getAccountNumber());
        jwtUserDTO.setAccountType(obj.getAccountType());
        jwtUserDTO.setPassword(obj.getPassword());
        jwtUserDTO.setId(obj.getId());
        jwtUserDTO.setFirstName(obj.getFirstName());
        jwtUserDTO.setLastName(obj.getLastName());
        jwtUserDTO.setStatus(obj.getStatus());
        return jwtUserDTO;
    }
}
