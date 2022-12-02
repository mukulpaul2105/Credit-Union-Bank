package com.CUBank.creditunionbank.utils;

import com.CUBank.creditunionbank.dtos.JWTUserDTO;
import com.CUBank.creditunionbank.dtos.JwtTokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry}")
    private Long expiry;

    public JwtTokenDTO generateToken(final JWTUserDTO userDTO) {
        final Map<String, Object> claims = new HashMap<>();
        claims.put("account", userDTO.getId());
        final LocalDateTime currentLDT = LocalDateTime.now();
        final LocalDateTime expiryLDT = currentLDT.plus(expiry, ChronoUnit.MINUTES);
        log.info("Current Time: ", currentLDT);
        log.info("Expiry Time in LocalDateTime form: ", expiryLDT);
        final Date expiryDate = Date.from(expiryLDT.atZone(ZoneId.systemDefault()).toInstant());
        log.info("Expiry Time in Date form: ", expiryDate);
        final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        final String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userDTO.getAccountNumber())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
        log.info("Generated Token: {} ", token);
        return new JwtTokenDTO(token, expiryLDT);
    }

    public Boolean validateToken(final String token, final JWTUserDTO userDTO) {
        log.info("Validating Token for User Details: {} with Token: ", token, " --> ", userDTO);
        final Claims claims = getAllClaims(token);
        final String accountNumber = claims.getSubject();
        final Date expiryDate = claims.getExpiration();
        final Date currentDate = new Date();

        if(StringUtils.hasText(accountNumber) && accountNumber.equals(userDTO.getAccountNumber()) &&
                        expiryDate.after(currentDate)) {
            return true;
        } else {
            return false;
        }
    }

    public String getAccountNumber(final String token) {
        return getAllClaims(token).getSubject();
    }

    public Long getUserId(final String token) {
        return getAllClaims(token).get("account", Long.class);
    }

    private Claims getAllClaims(final String token) {
        final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        final JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        return claimsJws.getBody();
    }
}
