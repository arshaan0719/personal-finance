package com.personalfinance.personal_finance.controller;

import com.personalfinance.personal_finance.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.personalfinance.personal_finance.entity.Transaction;
import java.util.List;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public Map<String, BigDecimal> getDashboard() {

        return Map.of(
                "totalIncome", dashboardService.getTotalIncome(),
                "totalExpenses", dashboardService.getTotalExpenses(),
                "netBalance", dashboardService.getNetBalance()
        );
    }

    @GetMapping("/recent")
    public List<Transaction> getRecentTransactions() {
        return dashboardService.getRecentTransactions();
    }
}