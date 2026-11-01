package com.example.expensetracker.repository;
import com.example.expensetracker.model.*; import org.springframework.data.jpa.repository.*; import java.time.LocalDate; import java.util.*;
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByTypeAndTransactionDateBetween(TransactionType type, LocalDate start, LocalDate end);
    List<Transaction> findByTransactionDateBetween(LocalDate start, LocalDate end);
}
