package com.haiilo.checkout.api.exception;

import com.haiilo.checkout.exception.UnknownProductException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Maps domain exceptions to HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnknownProductException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUnknownProduct(UnknownProductException ex) {
        return Map.of(
                "error", UnknownProductException.ERROR_CODE,
                "message", ex.getMessage()
        );
    }
}
