package com.example.expensetracker.exception;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(NotFoundException.class) @ResponseStatus(HttpStatus.NOT_FOUND) Map<String,Object> missing(NotFoundException e) { return error(HttpStatus.NOT_FOUND, e.getMessage()); }
 @ExceptionHandler(MethodArgumentNotValidException.class) @ResponseStatus(HttpStatus.BAD_REQUEST) Map<String,Object> invalid(MethodArgumentNotValidException e) { Map<String,String> fields = new LinkedHashMap<>(); e.getBindingResult().getFieldErrors().forEach(f -> fields.put(f.getField(), f.getDefaultMessage())); Map<String,Object> body = error(HttpStatus.BAD_REQUEST, "Validation failed"); body.put("fields", fields); return body; }
 @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST) Map<String,Object> bad(IllegalArgumentException e) { return error(HttpStatus.BAD_REQUEST, e.getMessage()); }
 @ExceptionHandler(Exception.class) @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) Map<String,Object> unexpected(Exception e) { return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"); }
 private Map<String,Object> error(HttpStatus status, String message) { Map<String,Object> b = new LinkedHashMap<>(); b.put("timestamp", Instant.now()); b.put("status", status.value()); b.put("message", message); return b; }
}
