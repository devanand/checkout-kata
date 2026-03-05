package com.haiilo.checkout.domain;

import java.util.Objects;

public record CartItem(ProductId productId, int quantity) {
    public CartItem {
        Objects.requireNonNull(productId, "productId must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }

    public CartItem increaseBy(int additionalQuantity) {
        if (additionalQuantity <= 0) {
            throw new IllegalArgumentException("additionalQuantity must be greater than zero");
        }
        return new CartItem(productId, quantity + additionalQuantity);
    }
}
