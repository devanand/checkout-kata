package com.haiilo.checkout.domain;

import java.util.Objects;

/**
 * Strongly-typed identifier for products.
 *
 *  Invariant: value is non-null, non-blank, trimmed, and normalized to uppercase.
 * This avoids primitive obsession and ensures consistent equality and map keys.
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
