package com.example.expensetracker.controller;
import com.example.expensetracker.dto.*; import com.example.expensetracker.service.BudgetService; import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/budgets")
public class BudgetController {
 private final BudgetService service; public BudgetController(BudgetService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public BudgetResponse create(@Valid @RequestBody BudgetRequest request){return service.create(request);}
 @GetMapping("/current") public BudgetResponse current(){return service.current();}
 @PutMapping("/{id}") public BudgetResponse update(@PathVariable Long id,@Valid @RequestBody BudgetRequest request){return service.update(id,request);}
}
