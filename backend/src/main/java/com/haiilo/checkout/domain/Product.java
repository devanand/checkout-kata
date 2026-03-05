package com.haiilo.checkout.domain;

import java.util.Objects;

/**
 * Represents a purchasable product in the catalog.
 *
 * A product is uniquely identified by its {@link ProductId} and has
 * a fixed unit price.
 *
 * Invariant:
 *   ProductId must not be null
 *   Unit price must be strictly greater than zero
 */
public record Product(ProductId productId, Money unitPrice) {
    public Product {
        Objects.requireNonNull(productId, "id must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");

        if (!unitPrice.isPositive()) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
    }
}
