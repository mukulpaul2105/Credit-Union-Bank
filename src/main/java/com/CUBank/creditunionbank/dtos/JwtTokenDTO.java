package com.CUBank.creditunionbank.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JwtTokenDTO {
    private String token;
    private LocalDateTime expiry;
}
