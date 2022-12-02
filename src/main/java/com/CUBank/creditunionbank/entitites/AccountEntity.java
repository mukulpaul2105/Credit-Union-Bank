package com.CUBank.creditunionbank.entitites;

import com.CUBank.creditunionbank.enums.IdentityType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class AccountEntity extends IDedEntity {

    @Column(name = "first_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String lastName;

    @Column(name = "middle_name", columnDefinition = "VARCHAR(50)")
    private String middleName;

    @Column(name = "identity_type", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private IdentityType identityType;

    @Column(name = "identity_number", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String identityNumber;

    @Column(name = "contact_number", nullable = false)
    private Long contactNumber;

    @Column(name = "email", columnDefinition = "VARCHAR(50)")
    private String email;


    @Column(name = "created_on", columnDefinition = "DATETIME")
    private LocalDateTime createdOn;

    @Column(name = "approved_by")
    private Long createdBy;
}
