package com.personalfinance.personal_finance.repository;

import com.personalfinance.personal_finance.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}