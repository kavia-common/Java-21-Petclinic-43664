package com.marcoslombog.mybank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Account entity representing a bank account.
 * Fields:
 * - id: primary key
 * - name: account holder name (required)
 * - balance: current balance
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private double balance;

    public Account() {
        // default constructor
    }

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    // PUBLIC_INTERFACE
    /**
     * Set the id. Typically managed by JPA and not set by clients.
     * @param id primary key
     */
    public void setId(Long id) {
        this.id = id;
    }

    // PUBLIC_INTERFACE
    /**
     * Returns the account holder's name.
     * @return name of the account holder
     */
    public String getName() {
        return name;
    }

    // PUBLIC_INTERFACE
    /**
     * Sets the account holder's name.
     * @param name account holder name
     */
    public void setName(String name) {
        this.name = name;
    }

    // PUBLIC_INTERFACE
    /**
     * Returns the current balance.
     * @return account balance
     */
    public double getBalance() {
        return balance;
    }

    // PUBLIC_INTERFACE
    /**
     * Sets the account balance.
     * @param balance new balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
