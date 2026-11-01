package com.example.expensetracker;
import com.example.expensetracker.dto.*; import com.example.expensetracker.model.TransactionType; import com.example.expensetracker.service.TransactionService; import org.junit.jupiter.api.*; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.test.context.SpringBootTest; import org.springframework.transaction.annotation.Transactional; import java.math.BigDecimal; import java.time.LocalDate; import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest @Transactional
class TransactionServiceTest {
 @Autowired TransactionService service;
 @Test void calculatesIncomeExpensesAndBalanceUsingBigDecimal(){service.create(new TransactionRequest(TransactionType.INCOME,new BigDecimal("1000.00"),"Salary",null,LocalDate.now()));service.create(new TransactionRequest(TransactionType.EXPENSE,new BigDecimal("250.50"),"Food",null,LocalDate.now()));DashboardSummary summary=service.summary();assertThat(summary.totalIncome()).isEqualByComparingTo("1000.00");assertThat(summary.totalExpenses()).isEqualByComparingTo("250.50");assertThat(summary.balance()).isEqualByComparingTo("749.50");}
}
