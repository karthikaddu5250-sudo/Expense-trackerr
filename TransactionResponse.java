package com.example.expensetracker.dto;
import com.example.expensetracker.model.*; import java.math.BigDecimal; import java.time.*;
public record TransactionResponse(Long id, TransactionType type, BigDecimal amount, String category, String description, LocalDate transactionDate, LocalDateTime createdAt, LocalDateTime updatedAt) {}
