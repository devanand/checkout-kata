package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PercentDiscountOfferTest {

    @Test
    void appliesPercentageDiscount() {
        PercentDiscountOffer offer = new PercentDiscountOffer(
                ProductId.of("BANANA"),
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
                10
        );

        assertEquals(Money.eur("0.54"), offer.priceFor(3, Money.eur("0.20")));
    }
}
