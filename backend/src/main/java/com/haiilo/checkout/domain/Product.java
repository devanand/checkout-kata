package com.haiilo.checkout.domain;

import java.util.Objects;

public record Product(ProductId productId, Money unitPrice) {
    public Product {
        Objects.requireNonNull(productId, "id must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");

        if (!unitPrice.isPositive()) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
    }
}
