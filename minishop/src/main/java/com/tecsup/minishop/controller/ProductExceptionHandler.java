package com.tecsup.minishop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice(assignableTypes = ProductController.class)
public class ProductExceptionHandler {
    // La guía del laboratorio exige HTTP 500 para un producto inexistente.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.internalServerError().body(Map.of("message", exception.getMessage()));
    }
}
