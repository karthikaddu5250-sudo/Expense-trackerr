package com.example.expensetracker.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record BudgetRequest(@Min(2020) @Max(2100) int year, @Min(1) @Max(12) int month, @NotNull @DecimalMin(value="0.01") @Digits(integer=17, fraction=2) BigDecimal amount) {}
