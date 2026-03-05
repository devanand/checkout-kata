package com.haiilo.checkout.domain;

import java.util.Objects;

/**
 * Strongly typed identifier for a product.
 *
 * This value object avoids using raw strings for product identifiers,
 * improving type safety and preventing accidental mix-ups with other IDs
 *
 * Invariant:
 *   Value must not be null
 *   Value must not be blank
 *   Value is normalized to uppercase
 */
public record ProductId(String value) {
    public ProductId {
        Objects.requireNonNull(value, "productId must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("productId must not be blank");
        }
        value = value.trim().toUpperCase();
    }

    public static ProductId of(String value) {
        return new ProductId(value);
    }
}
