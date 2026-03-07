package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.ProductId;

import java.time.LocalDate;

/**
 * Base implementation of the Offer interface providing common behavior.
 *
 * Handles shared attributes such as the associated product and
 * the validity period of the offer.
 */
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
