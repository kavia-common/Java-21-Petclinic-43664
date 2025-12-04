package com.marcoslombog.mybank.repository;

import com.marcoslombog.mybank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PUBLIC_INTERFACE
 * AccountRepository provides CRUD operations for Account entities using Spring Data JPA.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // No additional methods required for basic CRUD.
}
