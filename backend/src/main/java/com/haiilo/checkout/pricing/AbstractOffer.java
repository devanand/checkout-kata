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
    private final ValidityPeriod validityPeriod;

    protected AbstractOffer(ValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    @Override
    public boolean isActive(LocalDate date) {
        return validityPeriod.isActive(date);
    }
}
