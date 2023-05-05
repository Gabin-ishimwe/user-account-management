package com.app.advice;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationException {

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<?> handleSecurityException(JwtException ex) {
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                ex.getMessage(),
                null,
                HttpStatus.UNAUTHORIZED,
                errorTime
        ), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidArgument(MethodArgumentNotValidException methodArgumentNotValidException) {
        Map<String, String> errorMessages = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(
                fieldError -> {
                    errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
        );
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                "Failed validation",
                errorMessages,
                HttpStatus.BAD_REQUEST,
                errorTime
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception exception) {
        LocalDateTime errorTime = LocalDateTime.now();
        return new ResponseEntity<>(new ErrorDetails(
                exception.getMessage(),
                null,
                HttpStatus.INTERNAL_SERVER_ERROR,
                errorTime
        ), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
