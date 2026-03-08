package com.haiilo.checkout.application;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;

import java.util.Objects;

public record CheckoutLine(
        ProductId productId,
        int quantity,
        Money unitPrice,
        AppliedOfferSummary appliedOffer,
        Money lineTotal
) {
    public CheckoutLine {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        Objects.requireNonNull(lineTotal, "lineTotal must not be null");

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }
}