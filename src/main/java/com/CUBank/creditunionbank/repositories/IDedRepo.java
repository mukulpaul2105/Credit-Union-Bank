package com.CUBank.creditunionbank.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IDedRepo<T> extends JpaRepository<T, Long> {
}
