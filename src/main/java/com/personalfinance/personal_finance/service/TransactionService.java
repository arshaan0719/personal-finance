package com.personalfinance.personal_finance.service;


import com.personalfinance.personal_finance.entity.Account;
import com.personalfinance.personal_finance.entity.Category;
import com.personalfinance.personal_finance.entity.Transaction;
import com.personalfinance.personal_finance.entity.TransactionType;
import com.personalfinance.personal_finance.repository.AccountRepository;
import com.personalfinance.personal_finance.repository.CategoryRepository;
import com.personalfinance.personal_finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.personalfinance.personal_finance.exception.NotFoundException;
import com.personalfinance.personal_finance.exception.BadRequestException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository) {

        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Transaction> getTransactionsByAccount(Integer accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        validateTransaction(transaction);

        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        updateBalance(
                account,
                transaction.getTransactionType(),
                transaction.getAmount()
        );

        accountRepository.save(account);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    @Transactional
    public Transaction updateTransaction(Integer id, Transaction newTransaction) {

        validateTransaction(newTransaction);

        Transaction oldTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        Account oldAccount = accountRepository.findById(oldTransaction.getAccountId())
                .orElseThrow(() -> new NotFoundException("Old account not found"));

        reverseBalance(
                oldAccount,
                oldTransaction.getTransactionType(),
                oldTransaction.getAmount()
        );

        accountRepository.save(oldAccount);

        Account newAccount = accountRepository.findById(newTransaction.getAccountId())
                .orElseThrow(() -> new NotFoundException("New account not found"));

        updateBalance(
                newAccount,
                newTransaction.getTransactionType(),
                newTransaction.getAmount()
        );

        accountRepository.save(newAccount);

        newTransaction.setTransactionId(id);

        return transactionRepository.save(newTransaction);
    }

    @Transactional
    public void deleteTransaction(Integer id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        reverseBalance(
                account,
                transaction.getTransactionType(),
                transaction.getAmount()
        );

        accountRepository.save(account);

        transactionRepository.deleteById(id);
    }

    private void validateTransaction(Transaction transaction) {

        if (transaction.getAmount() == null ||
                transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new BadRequestException("Amount must be greater than zero");
        }

        if (transaction.getTransactionType() == null) {
            throw new BadRequestException("Transaction type is required");
        }

        if (transaction.getCategoryId() == null) {
            throw new BadRequestException("Category is required");
        }

        Category category = categoryRepository
                .findById(transaction.getCategoryId())
                .orElseThrow(() ->
                        new BadRequestException("Category not found"));

        if (!category.getCategoryType().name()
                .equals(transaction.getTransactionType().name())) {

            throw new BadRequestException(
                    "Transaction type does not match category type"
            );
        }
    }

    private void updateBalance(
            Account account,
            TransactionType type,
            BigDecimal amount) {

        if (type == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            account.setBalance(account.getBalance().subtract(amount));
        }
    }

    private void reverseBalance(
            Account account,
            TransactionType type,
            BigDecimal amount) {

        if (type == TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(amount));
        } else {
            account.setBalance(account.getBalance().add(amount));
        }
    }

    public List<Transaction> filterTransactions(
            Integer accountId,
            Integer categoryId,
            TransactionType type,
            LocalDate startDate,
            LocalDate endDate) {

        return transactionRepository.filterTransactions(
                accountId,
                categoryId,
                type,
                startDate,
                endDate
        );
    }
}