package com.personalfinance.personal_finance.repository;

import com.personalfinance.personal_finance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByUserId(Integer userId);
}