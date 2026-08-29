package com.personalfinance.personal_finance.controller;

import com.personalfinance.personal_finance.entity.Account;
import com.personalfinance.personal_finance.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/user/{userId}")
    public List<Account> getAccountsByUser(
            @PathVariable Integer userId) {

        return accountService.getAccountsByUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @PathVariable Integer id) {

        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable Integer id,
            @RequestBody Account account) {

        if (accountService.getAccountById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        account.setAccountId(id);

        return ResponseEntity.ok(
                accountService.updateAccount(account)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Integer id) {

        if (accountService.getAccountById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        accountService.deleteAccount(id);

        return ResponseEntity.noContent().build();
    }
}