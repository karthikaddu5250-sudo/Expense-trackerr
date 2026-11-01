package com.example.expensetracker.service;
import com.example.expensetracker.dto.*; import com.example.expensetracker.exception.NotFoundException; import com.example.expensetracker.model.Budget; import com.example.expensetracker.repository.BudgetRepository;
import org.springframework.stereotype.Service; import java.math.*; import java.time.LocalDate;
@Service
public class BudgetService {
 private final BudgetRepository budgets; private final TransactionService transactions;
 public BudgetService(BudgetRepository budgets, TransactionService transactions) { this.budgets=budgets; this.transactions=transactions; }
 public BudgetResponse create(BudgetRequest request) { if(budgets.findByBudgetYearAndBudgetMonth(request.year(),request.month()).isPresent()) throw new IllegalArgumentException("A budget already exists for this month. Update it instead."); Budget b=new Budget(); b.setBudgetYear(request.year()); b.setBudgetMonth(request.month()); b.setAmount(request.amount()); return response(budgets.save(b)); }
 public BudgetResponse current() { LocalDate today=LocalDate.now(); return budgets.findByBudgetYearAndBudgetMonth(today.getYear(),today.getMonthValue()).map(this::response).orElse(null); }
 public BudgetResponse update(Long id, BudgetRequest request) { Budget b=budgets.findById(id).orElseThrow(()->new NotFoundException("Budget " + id + " was not found")); if((b.getBudgetYear()!=request.year() || b.getBudgetMonth()!=request.month()) && budgets.findByBudgetYearAndBudgetMonth(request.year(),request.month()).isPresent()) throw new IllegalArgumentException("A budget already exists for this month"); b.setBudgetYear(request.year()); b.setBudgetMonth(request.month()); b.setAmount(request.amount()); return response(budgets.save(b)); }
 private BudgetResponse response(Budget b) { BigDecimal spent=transactions.expensesFor(b.getBudgetYear(),b.getBudgetMonth()); BigDecimal percent=spent.multiply(BigDecimal.valueOf(100)).divide(b.getAmount(),2,RoundingMode.HALF_UP); String status=spent.compareTo(b.getAmount())>0?"EXCEEDED":percent.compareTo(BigDecimal.valueOf(80))>=0?"WARNING":"ON_TRACK"; return new BudgetResponse(b.getId(),b.getBudgetYear(),b.getBudgetMonth(),b.getAmount(),spent,b.getAmount().subtract(spent),percent,status); }
}
