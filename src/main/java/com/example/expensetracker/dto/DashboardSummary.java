package com.example.expensetracker.dto;
import java.math.BigDecimal;
public record DashboardSummary(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal balance, BigDecimal currentMonthExpenses, long transactionCount) {}
