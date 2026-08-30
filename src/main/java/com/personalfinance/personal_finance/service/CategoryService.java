package com.personalfinance.personal_finance.service;

import com.personalfinance.personal_finance.entity.Category;
import com.personalfinance.personal_finance.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}