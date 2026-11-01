package com.example.expensetracker.dto;
import com.example.expensetracker.model.TransactionType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal; import java.time.LocalDate;
public record TransactionRequest(@NotNull TransactionType type, @NotNull @DecimalMin(value="0.01") @Digits(integer=17, fraction=2) BigDecimal amount, @NotBlank @Size(max=60) String category, @Size(max=500) String description, @NotNull LocalDate transactionDate) {}
