package com.personalfinance.personal_finance.controller;

import com.personalfinance.personal_finance.entity.Transaction;
import com.personalfinance.personal_finance.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.personalfinance.personal_finance.entity.TransactionType;

import java.time.LocalDate;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(
            @PathVariable Integer id) {

        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> getTransactionsByAccount(
            @PathVariable Integer accountId) {

        return transactionService.getTransactionsByAccount(accountId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Integer id,
            @RequestBody Transaction transaction) {

        if (transactionService.getTransactionById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        transaction.setTransactionId(id);

        return ResponseEntity.ok(
                transactionService.updateTransaction(id, transaction)
        );
    }

    @GetMapping("/filter")
    public List<Transaction> filterTransactions(
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        return transactionService.filterTransactions(
                accountId,
                categoryId,
                type,
                startDate,
                endDate
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Integer id) {

        if (transactionService.getTransactionById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        transactionService.deleteTransaction(id);

        return ResponseEntity.noContent().build();
    }



}