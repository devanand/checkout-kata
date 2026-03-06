package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;

import java.time.LocalDate;
import java.util.Objects;

public abstract class AbstractOffer implements Offer {
    private final ProductId productId;
    private final ValidityPeriod validityPeriod;

    protected AbstractOffer(ProductId productId, ValidityPeriod validityPeriod) {
        this.productId = productId;
        this.validityPeriod = validityPeriod;
    }

    @Override
    public ProductId getProductId() {
        return productId;
    }

    @Override
    public boolean isActive(LocalDate date) {
        return validityPeriod.isActive(date);
    }
}
