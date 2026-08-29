package com.personalfinance.personal_finance.repository;

import com.personalfinance.personal_finance.entity.Transaction;
import com.personalfinance.personal_finance.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.transactionType = :type")
    BigDecimal getTotalByType(@Param("type") TransactionType type);

    List<Transaction> findTop5ByOrderByTransactionDateDescTransactionIdDesc();

    List<Transaction> findByAccountId(Integer accountId);

    @Query("""
        SELECT t FROM Transaction t
        WHERE (:accountId IS NULL OR t.accountId = :accountId)
        AND (:categoryId IS NULL OR t.categoryId = :categoryId)
        AND (:type IS NULL OR t.transactionType = :type)
        AND (:startDate IS NULL OR t.transactionDate >= :startDate)
        AND (:endDate IS NULL OR t.transactionDate <= :endDate)
        ORDER BY t.transactionDate DESC, t.transactionId DESC
        """)
    List<Transaction> filterTransactions(
            @Param("accountId") Integer accountId,
            @Param("categoryId") Integer categoryId,
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}