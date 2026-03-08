package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiBuyOfferTest {

    private final MultiBuyOffer offer = new MultiBuyOffer(
            new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
            2,
            Money.eur("0.45")
    );

    @Test
    void appliesBundlePriceExactly() {
        assertEquals(Money.eur("0.45"), offer.priceFor(2, Money.eur("0.30")));
    }

    @Test
    void appliesBundleAndRemainder() {
        assertEquals(Money.eur("0.75"), offer.priceFor(3, Money.eur("0.30")));
    }

    @Test
    void appliesBundleMultipleTimes() {
        assertEquals(Money.eur("0.90"), offer.priceFor(4, Money.eur("0.30")));
    }
}
