package com.example.expensetracker.dto;
import java.math.BigDecimal;
public record MonthlyReport(int month, String label, BigDecimal income, BigDecimal expenses) {}
