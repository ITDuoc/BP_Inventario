package com.bookpoint.inventario.exception;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @Test
    void manejoErroresValidacion() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError error = new FieldError("obj", "nombre", "no puede estar vacío");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error));

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        Map<String, String> result = handler.manejoErroresValidacion(ex);

        assertEquals(1, result.size());
        assertEquals("no puede estar vacío", result.get("nombre"));
    }
}