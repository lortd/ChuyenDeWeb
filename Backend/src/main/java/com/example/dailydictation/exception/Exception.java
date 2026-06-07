package com.example.dailydictation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class Exception {
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> handlingRuntimeException(RuntimeException runtimeException){
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }
    @ExceptionHandler (value = MethodArgumentNotValidException.class)
    ResponseEntity<String>handlingValidation(MethodArgumentNotValidException exception){
        return ResponseEntity.badRequest().body(exception.getFieldError().getDefaultMessage());
    }
}
