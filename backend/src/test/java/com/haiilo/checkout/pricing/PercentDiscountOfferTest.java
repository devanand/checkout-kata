package com.haiilo.checkout.pricing;

import com.haiilo.checkout.application.AppliedOfferSummary;
import com.haiilo.checkout.domain.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PercentDiscountOfferTest {

    private final ValidityPeriod validityPeriod = new ValidityPeriod(
            LocalDate.of(2026, 3, 1),
            LocalDate.of(2026, 3, 31)
    );

    @Test
    void appliesPercentageDiscountCorrectly() {
        PercentDiscountOffer offer = new PercentDiscountOffer(
                OfferType.PERCENT_DISCOUNT,
                "10% discount on bananas",
                validityPeriod,
                10
        );

        Money total = offer.priceFor(3, Money.eur("0.20"));

        assertEquals(Money.eur("0.54"), total);
    }

    @Test
    void appliesDiscountToZeroQuantity() {
        PercentDiscountOffer offer = new PercentDiscountOffer(
                OfferType.PERCENT_DISCOUNT,
                "10% discount on bananas",
                validityPeriod,
                10
        );

        Money total = offer.priceFor(0, Money.eur("0.20"));

        assertEquals(Money.zero(), total);
    }

    @Test
    void rejectsPercentageZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new PercentDiscountOffer(
                        OfferType.PERCENT_DISCOUNT,
                        "10% discount on bananas",
                        validityPeriod,
                        0
                )
        );
    }

    @Test
    void rejectsNegativePercentage() {
        assertThrows(IllegalArgumentException.class, () ->
                new PercentDiscountOffer(
                        OfferType.PERCENT_DISCOUNT,
                        "10% discount on bananas",
                        validityPeriod,
                        -1
                )
        );
    }

    @Test
    void rejectsPercentageHundred() {
        assertThrows(IllegalArgumentException.class, () ->
                new PercentDiscountOffer(
                        OfferType.PERCENT_DISCOUNT,
                        "10% discount on bananas",
                        validityPeriod,
                        100
                )
        );
    }

    @Test
    void rejectsPercentageAboveHundred() {
        assertThrows(IllegalArgumentException.class, () ->
                new PercentDiscountOffer(
                        OfferType.PERCENT_DISCOUNT,
                        "10% discount on bananas",
                        validityPeriod,
                        101
                )
        );
    }

    @Test
    void rejectsNegativeQuantity() {
        PercentDiscountOffer offer = new PercentDiscountOffer(
                OfferType.PERCENT_DISCOUNT,
                "10% discount on bananas",
                validityPeriod,
                10
        );

        assertThrows(IllegalArgumentException.class,
                () -> offer.priceFor(-1, Money.eur("0.20")));
    }

    @Test
    void rejectsNullUnitPrice() {
        PercentDiscountOffer offer = new PercentDiscountOffer(
                OfferType.PERCENT_DISCOUNT,
                "10% discount on bananas",
                validityPeriod,
                10
        );

        assertThrows(NullPointerException.class,
                () -> offer.priceFor(1, null));
    }

    @Test
    void returnsAppliedOfferSummaryFromOfferMetadata() {
        PercentDiscountOffer offer = new PercentDiscountOffer(
                OfferType.PERCENT_DISCOUNT,
                "10% discount on bananas",
                validityPeriod,
                10
        );

        AppliedOfferSummary summary = offer.toAppliedOfferSummary();

        assertEquals("PERCENT_DISCOUNT", summary.type());
        assertEquals("10% discount on bananas", summary.description());
    }
}
