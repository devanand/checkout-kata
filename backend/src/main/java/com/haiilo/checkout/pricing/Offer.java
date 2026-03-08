package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;

import java.time.LocalDate;

/**
 * Pricing rule that can modify the total price of a product based
 * on predefined conditions such as quantity or discount percentage.
 *
 * Implementations define how pricing adjustments are calculated.
 */
public interface Offer {
    boolean isActive(LocalDate date);
    Money priceFor(int quantity, Money unitPrice);
}
