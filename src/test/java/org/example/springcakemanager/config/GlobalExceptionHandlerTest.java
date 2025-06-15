package org.example.springcakemanager.config;

import org.example.springcakemanager.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleNotFound(new ResourceNotFoundException("missing"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().get("message").toString().contains("missing"));
    }

    @Test
    void handleBadRequest_returns400() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleBadRequest(new IllegalArgumentException("bad"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("message").toString().contains("bad"));
    }

    @Test
    void handleGeneric_returns500() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleGeneric(new RuntimeException("boom"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
