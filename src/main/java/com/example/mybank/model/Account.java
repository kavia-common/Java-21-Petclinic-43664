package com.example.mybank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * PUBLIC_INTERFACE
 * Account entity representing a simple bank account.
 * Mirrors my-bank's entity structure and table mapping (accounts).
 */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private Double balance;

    public Account() {
        // default constructor
    }

    public Account(String name, Double balance) {
        this.setName(name);
        this.setBalance(balance);
    }

    public Account(Long id, String name, Double balance) {
        this.setId(id);
        this.setName(name);
        this.setBalance(balance);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", name='" + name + '\'' + ", balance='" + balance + '\'' + '}';
    }
}
