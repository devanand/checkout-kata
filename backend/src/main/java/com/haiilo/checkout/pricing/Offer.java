package com.haiilo.checkout.pricing;

import com.haiilo.checkout.application.AppliedOfferSummary;
import com.haiilo.checkout.domain.Money;

import java.time.LocalDate;

/**
 * Pricing rule that can calculate the total price for a product quantity.
 */
public interface Offer {

    OfferType type();

    boolean isActive(LocalDate date);

    Money priceFor(int quantity, Money unitPrice);

    AppliedOfferSummary toAppliedOfferSummary();
}
