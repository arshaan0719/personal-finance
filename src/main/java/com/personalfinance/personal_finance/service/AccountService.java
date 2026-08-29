package com.personalfinance.personal_finance.service;

import com.personalfinance.personal_finance.entity.Account;
import com.personalfinance.personal_finance.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {

        if (account.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAccountsByUser(Integer userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> getAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(Integer id) {
        accountRepository.deleteById(id);
    }
}