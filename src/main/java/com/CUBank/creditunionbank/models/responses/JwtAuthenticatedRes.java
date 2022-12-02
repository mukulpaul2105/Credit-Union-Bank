package com.CUBank.creditunionbank.models.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JwtAuthenticatedRes {

    private String token;
    private LocalDateTime expiry;
}
