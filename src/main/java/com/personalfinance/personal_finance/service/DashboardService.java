package com.personalfinance.personal_finance.service;

import com.personalfinance.personal_finance.entity.TransactionType;
import com.personalfinance.personal_finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import com.personalfinance.personal_finance.entity.Transaction;
import java.util.List;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getTotalIncome() {
        return transactionRepository.getTotalByType(TransactionType.INCOME);
    }

    public BigDecimal getTotalExpenses() {
        return transactionRepository.getTotalByType(TransactionType.EXPENSE);
    }

    public BigDecimal getNetBalance() {
        return getTotalIncome().subtract(getTotalExpenses());
    }

    public List<Transaction> getRecentTransactions() {
        return transactionRepository.findTop5ByOrderByTransactionDateDescTransactionIdDesc();
    }
}