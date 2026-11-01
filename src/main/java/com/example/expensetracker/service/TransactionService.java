package com.example.expensetracker.service;
import com.example.expensetracker.dto.*; import com.example.expensetracker.exception.NotFoundException; import com.example.expensetracker.model.*; import com.example.expensetracker.repository.TransactionRepository;
import org.springframework.data.domain.*; import org.springframework.data.jpa.domain.Specification; import org.springframework.stereotype.Service; import java.math.BigDecimal; import java.time.*; import java.util.*;
@Service
public class TransactionService {
 private final TransactionRepository repository; public TransactionService(TransactionRepository repository) { this.repository = repository; }
 public TransactionResponse create(TransactionRequest request) { Transaction t = new Transaction(); copy(request, t); return response(repository.save(t)); }
 public TransactionResponse get(Long id) { return response(find(id)); }
 public TransactionResponse update(Long id, TransactionRequest request) { Transaction t = find(id); copy(request, t); return response(repository.save(t)); }
 public void delete(Long id) { repository.delete(find(id)); }
 public Page<TransactionResponse> list(TransactionType type, String category, LocalDate startDate, LocalDate endDate, String search, String sortBy, String direction, int page, int size) {
   if (startDate != null && endDate != null && startDate.isAfter(endDate)) throw new IllegalArgumentException("startDate must be on or before endDate");
   String field = Set.of("transactionDate", "amount", "createdAt").contains(sortBy) ? sortBy : "transactionDate";
   Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
   Specification<Transaction> spec = Specification.where(null);
   if (type != null) spec = spec.and((r,q,c) -> c.equal(r.get("type"), type));
   if (category != null && !category.isBlank()) spec = spec.and((r,q,c) -> c.equal(r.get("category"), category));
   if (startDate != null) spec = spec.and((r,q,c) -> c.greaterThanOrEqualTo(r.get("transactionDate"), startDate));
   if (endDate != null) spec = spec.and((r,q,c) -> c.lessThanOrEqualTo(r.get("transactionDate"), endDate));
   if (search != null && !search.isBlank()) { String value = "%" + search.toLowerCase() + "%"; spec = spec.and((r,q,c) -> c.like(c.lower(r.get("description")), value)); }
   return repository.findAll(spec, PageRequest.of(Math.max(page,0), Math.min(Math.max(size,1),100), Sort.by(dir, field))).map(this::response);
 }
 public DashboardSummary summary() { List<Transaction> all = repository.findAll(); BigDecimal income=sum(all, TransactionType.INCOME), expense=sum(all, TransactionType.EXPENSE); LocalDate now=LocalDate.now(); BigDecimal month=sum(repository.findByTypeAndTransactionDateBetween(TransactionType.EXPENSE, now.withDayOfMonth(1), now.withDayOfMonth(now.lengthOfMonth())), TransactionType.EXPENSE); return new DashboardSummary(income, expense, income.subtract(expense), month, all.size()); }
 public List<MonthlyReport> monthly(int year) { List<MonthlyReport> result=new ArrayList<>(); for(int m=1;m<=12;m++){ LocalDate d=LocalDate.of(year,m,1); List<Transaction> items=repository.findByTransactionDateBetween(d,d.withDayOfMonth(d.lengthOfMonth())); result.add(new MonthlyReport(m,d.getMonth().toString().substring(0,3),sum(items,TransactionType.INCOME),sum(items,TransactionType.EXPENSE))); } return result; }
 public List<CategoryReport> categories() { Map<String,BigDecimal> values=new TreeMap<>(); repository.findAll().stream().filter(t->t.getType()==TransactionType.EXPENSE).forEach(t->values.merge(t.getCategory(),t.getAmount(),BigDecimal::add)); return values.entrySet().stream().map(e->new CategoryReport(e.getKey(),e.getValue())).toList(); }
 public BigDecimal expensesFor(int year,int month) { LocalDate start=LocalDate.of(year,month,1); return sum(repository.findByTypeAndTransactionDateBetween(TransactionType.EXPENSE,start,start.withDayOfMonth(start.lengthOfMonth())),TransactionType.EXPENSE); }
 private Transaction find(Long id) { return repository.findById(id).orElseThrow(()->new NotFoundException("Transaction " + id + " was not found")); }
 private void copy(TransactionRequest r, Transaction t) { t.setType(r.type()); t.setAmount(r.amount()); t.setCategory(r.category().trim()); t.setDescription(r.description()==null?null:r.description().trim()); t.setTransactionDate(r.transactionDate()); }
 private TransactionResponse response(Transaction t) { return new TransactionResponse(t.getId(),t.getType(),t.getAmount(),t.getCategory(),t.getDescription(),t.getTransactionDate(),t.getCreatedAt(),t.getUpdatedAt()); }
 private BigDecimal sum(List<Transaction> tx, TransactionType type) { return tx.stream().filter(t->t.getType()==type).map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add); }
}
