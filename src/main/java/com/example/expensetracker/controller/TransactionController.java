package com.example.expensetracker.controller;
import com.example.expensetracker.dto.*; import com.example.expensetracker.model.TransactionType; import com.example.expensetracker.service.TransactionService;
import jakarta.validation.Valid; import org.springframework.data.domain.Page; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.LocalDate;
@RestController @RequestMapping("/api/transactions")
public class TransactionController {
 private final TransactionService service; public TransactionController(TransactionService service) { this.service=service; }
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public TransactionResponse create(@Valid @RequestBody TransactionRequest request) { return service.create(request); }
 @GetMapping public Page<TransactionResponse> list(@RequestParam(required=false) TransactionType type,@RequestParam(required=false) String category,@RequestParam(required=false) LocalDate startDate,@RequestParam(required=false) LocalDate endDate,@RequestParam(required=false) String search,@RequestParam(defaultValue="transactionDate") String sortBy,@RequestParam(defaultValue="desc") String direction,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="20") int size) { return service.list(type,category,startDate,endDate,search,sortBy,direction,page,size); }
 @GetMapping("/{id}") public TransactionResponse get(@PathVariable Long id) { return service.get(id); }
 @PutMapping("/{id}") public TransactionResponse update(@PathVariable Long id,@Valid @RequestBody TransactionRequest request) { return service.update(id,request); }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
