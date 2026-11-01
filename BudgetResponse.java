package com.example.expensetracker.dto;
import java.math.BigDecimal;
public record BudgetResponse(Long id, int year, int month, BigDecimal budgetAmount, BigDecimal spent, BigDecimal remaining, BigDecimal progressPercent, String status) {}
