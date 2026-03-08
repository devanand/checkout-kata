package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;

import java.util.Objects;

/**
 * Offer that applies a bundled price when a required quantity
 * of a product is purchased.
 *
 * Example:
 * 2 apples for 0.45 instead of paying individual unit prices.
 */
public class MultiBuyOffer extends AbstractOffer {
    private final int requiredQuantity;
    private final Money bundlePrice;

    public MultiBuyOffer(ValidityPeriod validityPeriod, int requiredQuantity, Money bundlePrice) {
        super(validityPeriod);
        if (requiredQuantity <= 1) {
            throw new IllegalArgumentException("required quantity must be greater than 1");
        }
        this.bundlePrice = bundlePrice;
        if (!bundlePrice.isPositive()) {
            throw new IllegalArgumentException("bundleprice must be greater than zero");
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
