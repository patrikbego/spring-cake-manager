package org.example.springcakemanager.config;

import org.example.springcakemanager.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        Map<String, Object> body = Map.of(
                TIMESTAMP, LocalDateTime.now(),
                STATUS, HttpStatus.BAD_REQUEST.value(),
                ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                MESSAGE, "Validation error",
                "fields", fieldErrors
        );
        logger.warn("Validation error: {}", fieldErrors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", ex);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String message, Exception ex) {
        logger.error("Error occurred: {}", message, ex);
        Map<String, Object> body = Map.of(
                TIMESTAMP, LocalDateTime.now(),
                STATUS, status.value(),
                ERROR, status.getReasonPhrase(),
                MESSAGE, message
        );
        return new ResponseEntity<>(body, status);
    }
}
