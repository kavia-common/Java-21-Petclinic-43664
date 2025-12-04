package com.marcoslombog.mybank.controller;

import com.marcoslombog.mybank.exception.ResourceNotFoundException;
import com.marcoslombog.mybank.model.Account;
import com.marcoslombog.mybank.repository.AccountRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * AccountController exposes REST endpoints for managing bank accounts.
 * Preserved endpoints:
 * - GET  /accounts/all
 * - GET  /accounts/{id}
 * - POST /accounts/new
 * - PUT  /accounts/{id}?amount=<double>
 */
@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Operations for bank accounts")
public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    // PUBLIC_INTERFACE
    @GetMapping("/all")
    @Operation(summary = "List all accounts", description = "Returns all bank accounts.")
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    // PUBLIC_INTERFACE
    @GetMapping("/{id}")
    @Operation(summary = "Get account by id", description = "Returns the account for the provided id.")
    public Account getAccountById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    }

    // PUBLIC_INTERFACE
    @PostMapping("/new")
    @Operation(summary = "Create new account", description = "Creates a new account with the given name and balance.")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account newAccount) {
        Account saved = repository.save(newAccount);
        return ResponseEntity.ok(saved);
    }

    // PUBLIC_INTERFACE
    @PutMapping("/{id}")
    @Operation(
        summary = "Update account balance",
        description = "Increments the account balance by the specified amount (query parameter 'amount')."
    )
    public ResponseEntity<Account> updateBalance(
            @PathVariable Long id,
            @Parameter(description = "Amount to add (can be negative to subtract).", required = true)
            @RequestParam(name = "amount") double amount) {

        Account account = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));

        account.setBalance(account.getBalance() + amount);
        Account updated = repository.save(account);
        return ResponseEntity.ok(updated);
    }
}
