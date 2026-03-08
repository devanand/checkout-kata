package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;

import java.util.Objects;

/**
 * Offer that applies a bundled price when a required quantity
 * of a product is purchased.
 */
public final class MultiBuyOffer extends AbstractOffer {

    private final int requiredQuantity;
    private final Money bundlePrice;

    public MultiBuyOffer(
            OfferType type,
            String description,
            ValidityPeriod validityPeriod,
            int requiredQuantity,
            Money bundlePrice
    ) {
        super(type, description, validityPeriod);

        if (requiredQuantity <= 1) {
            throw new IllegalArgumentException("required quantity must be greater than 1");
        }

        this.bundlePrice = Objects.requireNonNull(bundlePrice, "bundlePrice must not be null");

        if (!bundlePrice.isPositive()) {
            throw new IllegalArgumentException("bundlePrice must be greater than zero");
        }

        this.requiredQuantity = requiredQuantity;
    }

    @Override
    public Money priceFor(int quantity, Money unitPrice) {
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");

        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }

        int bundleCount = quantity / requiredQuantity;
        int remainder = quantity % requiredQuantity;

        Money bundledTotal = bundlePrice.times(bundleCount);
        Money remainderTotal = unitPrice.times(remainder);

        return bundledTotal.plus(remainderTotal);
    }
}