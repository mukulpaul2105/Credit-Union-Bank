package com.CUBank.creditunionbank.dtos;

import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class AccountDTO extends IDedDTO{

    private String firstName;
    private String lastName;
    private String middleName;
    private IdentityType identityType;
    private String identityNumber;
    private Long contactNumber;
    private String email;
    private LocalDateTime createdOn;
    private Long createdBy;
}
