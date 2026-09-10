package br.com.db.system.votingsystem.v1.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void shouldMapResourceNotFoundTo404() {
        ResponseEntity<String> response = handler.handleResourceNotFound(new ResourceNotFoundException("not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("not found", response.getBody());
    }

    @Test
    void shouldMapInvalidRequestTo400() {
        ResponseEntity<String> response = handler.handleInvalidRequest(new InvalidRequestException("bad request"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("bad request", response.getBody());
    }

    @Test
    void shouldMapBusinessRuleTo422() {
        ResponseEntity<String> response = handler.handleBusinessRule(new BusinessRuleException("rule broken"));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("rule broken", response.getBody());
    }

    @Test
    void shouldMapDataIntegrityViolationTo409() {
        ResponseEntity<String> response = handler.handleDataIntegrityViolation(
                new DataIntegrityViolationException("duplicate key"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldMapUnexpectedExceptionTo500WithoutLeakingDetails() {
        ResponseEntity<String> response = handler.handleGenericException(new RuntimeException("boom, stacktrace, internals"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertFalse(response.getBody().contains("boom"));
    }

    @Test
    void shouldMapValidationErrorsToFieldMessages() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("agendaDTO", "description", "Description must not be null or blank");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Description must not be null or blank", response.getBody().get("description"));
    }
}
