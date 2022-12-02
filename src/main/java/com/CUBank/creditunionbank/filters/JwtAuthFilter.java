package com.CUBank.creditunionbank.filters;

import com.CUBank.creditunionbank.dtos.*;
import com.CUBank.creditunionbank.exceptions.NoCoDAccountFoundException;
import com.CUBank.creditunionbank.exceptions.NoMoneyMarketAccountFoundException;
import com.CUBank.creditunionbank.services.IAdminService;
import com.CUBank.creditunionbank.services.ICodService;
import com.CUBank.creditunionbank.services.IMoneyMarketService;
import com.CUBank.creditunionbank.utils.JwtUtils;
import com.CUBank.creditunionbank.utils.LoggedInContext;
import com.CUBank.creditunionbank.utils.MapperUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IMoneyMarketService moneyMarketService;

    @Autowired
    private ICodService codService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String authTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authTokenHeader)) {
            log.info("No Authorization Token Found");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), " UNAUTHORIZED");
            return;
        }

        if (!authTokenHeader.startsWith("Bearer")) {
            log.info("No Bearer Token found in Authorization");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), " UNAUTHORIZED");
            return;
        }

        final String jwtToken = authTokenHeader.substring(7);

        if (!StringUtils.hasText(authTokenHeader)) {
            log.info("No Bearer Token Found in Authentication");
            response.sendError(HttpStatus.UNAUTHORIZED.value(), " UNAUTHORIZED");
            return;
        }

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                final String accountNumber = jwtUtil.getAccountNumber(jwtToken);
                JWTUserDTO jwtUserDTO = new JWTUserDTO();
                if(accountNumber.startsWith("33")) {
                    log.info("Finding admin account with account number: ", accountNumber);
                    final AdminDTO userDTO = adminService.getByAccountNumber(accountNumber)
                            .orElseThrow(() -> new UsernameNotFoundException("User with accountNumber " + accountNumber + " not found while Authorizing"));
                    jwtUserDTO = jwtUserDTO.copyOf(MapperUtil.convertTo(userDTO, JWTUserDTO.class));

                } else if (accountNumber.startsWith("44") || accountNumber.startsWith("77")) {
                    log.info("Finding Money Market account with account number: ", accountNumber);
                    final MoneyMarketDTO userDTO = moneyMarketService.getByAccountNumber(accountNumber);
                    jwtUserDTO = jwtUserDTO.copyOf(MapperUtil.convertTo(userDTO, JWTUserDTO.class));

                } else if(accountNumber.startsWith("11")) {
                    log.info("Finding COD account with account number: ", accountNumber);
                    final CoDDTO userDTO = codService.getByAccountNumber(accountNumber);
                    jwtUserDTO = jwtUserDTO.copyOf(MapperUtil.convertTo(userDTO, JWTUserDTO.class));
                } else {
                    log.info("Authentication Failed");
                }
                log.info("JWT User DTO: ", jwtUserDTO.getStatus(), ", ", jwtUserDTO.getAccountType());
                if(jwtUtil.validateToken(jwtToken, jwtUserDTO)) {
                    log.info("JWT {} validated: ", jwtToken);
                    final UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO(jwtUserDTO);
                    final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userPrincipalDTO, null, userPrincipalDTO.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    LoggedInContext.setCurrentUser(jwtUserDTO.getAccountNumber());
                } else {
                    log.info("JWT {} Invalid: ", jwtToken);
                }

            } catch (final ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                log.info("Error Authentication JWT: \n", e);
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED");
                return;
            } catch (NoCoDAccountFoundException e) {
                log.error("No COD account found during authorization");
                throw new UsernameNotFoundException("No COD account found during authorization");
            } catch (NoMoneyMarketAccountFoundException e) {
                log.error("No Money Market account found during authorization");
                throw new UsernameNotFoundException("No Money Market account found during authorization");
            } catch (Exception e) {
                log.error("Exception occurred during authorization");
                throw new RuntimeException();
            }
        }

        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        final String requestPath = request.getRequestURI();
        if("/authenticate".equals(requestPath) || "/openAccount/openNow".equals(requestPath)
                || "/v2/api-docs".equals(requestPath)) {
            return true;
        }
        return false;
    }
}
