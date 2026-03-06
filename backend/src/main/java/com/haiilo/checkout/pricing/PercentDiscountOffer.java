package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;

import java.util.Objects;

public final class PercentDiscountOffer extends AbstractOffer {

    private final int percentage;

    public PercentDiscountOffer(
            ProductId productId,
            ValidityPeriod validityPeriod,
            int percentage
    ) {
        super(productId, validityPeriod);

        if (percentage <= 0 || percentage >= 100) {
            throw new IllegalArgumentException("percentage must be between 1 and 99");
        }

        this.percentage = percentage;
    }

    @Override
    public Money priceFor(int quantity, Money unitPrice) {
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");

        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        Money regularTotal = unitPrice.times(quantity);
        return regularTotal.applyPercentageDiscount(percentage);
    }
}
