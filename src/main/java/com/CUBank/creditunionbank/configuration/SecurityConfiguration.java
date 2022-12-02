package com.CUBank.creditunionbank.configuration;

import com.CUBank.creditunionbank.enums.AccountType;
import com.CUBank.creditunionbank.filters.JwtAuthFilter;
import com.CUBank.creditunionbank.services.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userPrincipalService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors().disable().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/openAccount/openNow").permitAll()
                .antMatchers("/openAccount/**").hasAuthority(AccountType.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/transaction/**")
                    .hasAnyAuthority(AccountType.ADMIN.name(), AccountType.CONSUMER.name(), AccountType.CERTIFICATE_OF_DEPOSIT.name(), AccountType.COMMERCIAL.name())
                .antMatchers(HttpMethod.GET, "/moneyMarket/account/**").hasAnyAuthority(AccountType.CONSUMER.name(), AccountType.COMMERCIAL.name())
                .antMatchers(HttpMethod.GET, "/moneyMarket/accounts/**").hasAuthority(AccountType.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/cod/account/**").hasAnyAuthority(AccountType.CERTIFICATE_OF_DEPOSIT.name())
                .antMatchers(HttpMethod.GET, "/cod/accounts/**").hasAuthority(AccountType.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/cod").hasAuthority(AccountType.ADMIN.name())
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
