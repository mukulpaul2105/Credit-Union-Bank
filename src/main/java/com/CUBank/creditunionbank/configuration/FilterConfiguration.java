package com.CUBank.creditunionbank.configuration;

import com.CUBank.creditunionbank.filters.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    @Autowired
    @Bean(name = "jwtFilterRegistration")
    public FilterRegistrationBean<JwtAuthFilter> jwtAFRB(final JwtAuthFilter jwtAF) {
        final FilterRegistrationBean<JwtAuthFilter> jwtAuthFRB = new FilterRegistrationBean<>();
        jwtAuthFRB.setFilter(jwtAF);
        return jwtAuthFRB;
    }
}
