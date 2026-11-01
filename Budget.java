package com.example.expensetracker.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets", uniqueConstraints = @UniqueConstraint(columnNames = {"budgetYear", "budgetMonth"}))
public class Budget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private int budgetYear;
    @Column(nullable = false) private int budgetMonth;
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal amount;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;
    @PrePersist void created() { createdAt = LocalDateTime.now(); updatedAt = createdAt; }
    @PreUpdate void updated() { updatedAt = LocalDateTime.now(); }
    public Long getId() { return id; } public int getBudgetYear() { return budgetYear; } public void setBudgetYear(int v) { budgetYear = v; }
    public int getBudgetMonth() { return budgetMonth; } public void setBudgetMonth(int v) { budgetMonth = v; }
    public BigDecimal getAmount() { return amount; } public void setAmount(BigDecimal v) { amount = v; }
}
