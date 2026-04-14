package com.gymflow.infrastructure.adapters.in.web;

import com.gymflow.domain.exceptions.EmailDuplicadoException;
import com.gymflow.domain.exceptions.PagosPendientesException;
import com.gymflow.domain.exceptions.SocioNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleEmailDuplicado(EmailDuplicadoException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(SocioNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleSocioNoEncontrado(SocioNoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PagosPendientesException.class)
    public ResponseEntity<Map<String, Object>> handlePagosPendientes(PagosPendientesException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}