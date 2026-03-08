package com.haiilo.checkout.pricing;

import com.haiilo.checkout.application.AppliedOfferSummary;
import com.haiilo.checkout.domain.Money;

import java.util.Objects;

public record PricingResult(
        Money lineTotal,
        AppliedOfferSummary appliedOffer
) {
    public PricingResult {
        Objects.requireNonNull(lineTotal, "lineTotal must not be null");
    }
}
