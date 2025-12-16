package com.example.mybank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mybank.model.Account;

/**
 * PUBLIC_INTERFACE
 * Repository interface for Account entity using Spring Data JPA.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
