package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductIdTest {

    @Test
    void normalizesValueToUppercaseAndTrim() {
        ProductId productId = ProductId.of(" apple   ");
        assertEquals(ProductId.of("APPLE"), productId);
        assertEquals("APPLE", productId.value());
    }

    @Test
    void rejectsNull() {
        assertThrows(NullPointerException.class, () -> ProductId.of(null));
    }

    @Test
    void rejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> ProductId.of("    "));
    }
}
