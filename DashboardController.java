package com.example.expensetracker.controller;
import com.example.expensetracker.dto.*; import com.example.expensetracker.service.TransactionService; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.*;
@RestController @RequestMapping("/api")
public class DashboardController {
 private final TransactionService service; public DashboardController(TransactionService service){this.service=service;}
 @GetMapping("/dashboard/summary") public DashboardSummary summary(){return service.summary();}
 @GetMapping("/reports/monthly") public List<MonthlyReport> monthly(@RequestParam(required=false) Integer year){return service.monthly(year==null?LocalDate.now().getYear():year);}
 @GetMapping("/reports/categories") public List<CategoryReport> categories(){return service.categories();}
}
