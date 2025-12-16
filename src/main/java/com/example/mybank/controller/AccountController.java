package com.example.mybank.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybank.exception.ResourceNotFoundException;
import com.example.mybank.model.Account;
import com.example.mybank.repository.AccountRepository;

/**
 * PUBLIC_INTERFACE
 * REST controller exposing CRUD endpoints aligned to my-bank.
 * Endpoints:
 *  - GET    /accounts/all
 *  - GET    /accounts/{id}
 *  - POST   /accounts/new
 *  - PUT    /accounts/{id} (amount as query param or numeric body)
 *  - DELETE /accounts/{id}
 */
@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * PUBLIC_INTERFACE
     * Returns all accounts.
     * @return list of accounts
     */
    @GetMapping("/accounts/all")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * PUBLIC_INTERFACE
     * Returns an account by id.
     * @param accountId the id path variable
     * @return the account
     * @throws ResourceNotFoundException if not found
     */
    @GetMapping("/accounts/{id}")
    public Account getAccountById(@PathVariable(value = "id") Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));
    }

    /**
     * PUBLIC_INTERFACE
     * Creates a new account.
     * @param account validated account payload
     * @return the created account
     */
    @PostMapping("/accounts/new")
    public Account createAccount(@Valid @RequestBody Account account) {
        return accountRepository.save(account);
    }

    /**
     * PUBLIC_INTERFACE
     * Updates the balance by adding delta amount (from query param or numeric body).
     * If no amount provided, returns current account unchanged (conservative no-op).
     * @param accountId account id
     * @param amountParam optional delta from query param
     * @param amountBody optional delta from numeric body
     * @return updated account
     */
    @PutMapping("/accounts/{id}")
    public Account updateAmount(
            @PathVariable(value = "id") Long accountId,
            @RequestParam(value = "amount", required = false) Double amountParam,
            @RequestBody(required = false) Double amountBody) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        // Support both original request param and a simple numeric body for amount
        Double delta = amountParam != null ? amountParam : (amountBody != null ? amountBody : null);
        if (delta == null) {
            // No amount provided - keep behavior conservative; no-op update
            return account;
        }

        account.setBalance(account.getBalance() + delta);
        Account updatedAccount = accountRepository.save(account);
        return updatedAccount;
    }

    /**
     * PUBLIC_INTERFACE
     * Deletes an account by id.
     * @param accountId id of the account
     * @return 200 OK response entity
     */
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable(value = "id") Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        accountRepository.delete(account);
        return ResponseEntity.ok().build();
    }
}
