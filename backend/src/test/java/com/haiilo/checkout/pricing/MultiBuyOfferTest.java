package com.haiilo.checkout.pricing;

import com.haiilo.checkout.application.AppliedOfferSummary;
import com.haiilo.checkout.domain.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MultiBuyOfferTest {

    private final ValidityPeriod validityPeriod = new ValidityPeriod(
            LocalDate.of(2026, 3, 1),
            LocalDate.of(2026, 3, 31)
    );

    @Test
    void appliesBundlePriceExactly() {
        MultiBuyOffer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                validityPeriod,
                2,
                Money.eur("0.45")
        );

        Money total = offer.priceFor(2, Money.eur("0.30"));

        assertEquals(Money.eur("0.45"), total);
    }

    @Test
    void appliesBundleAndRemainderPrice() {
        MultiBuyOffer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                validityPeriod,
                2,
                Money.eur("0.45")
        );

        Money total = offer.priceFor(3, Money.eur("0.30"));

        assertEquals(Money.eur("0.75"), total);
    }

    @Test
    void appliesBundleMultipleTimes() {
        MultiBuyOffer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                validityPeriod,
                2,
                Money.eur("0.45")
        );

        Money total = offer.priceFor(4, Money.eur("0.30"));

        assertEquals(Money.eur("0.90"), total);
    }

    @Test
    void usesRegularUnitPriceWhenQuantityIsLessThanRequiredQuantity() {
        MultiBuyOffer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                validityPeriod,
                2,
                Money.eur("0.45")
        );

        Money total = offer.priceFor(1, Money.eur("0.30"));

        assertEquals(Money.eur("0.30"), total);
    }

    @Test
    void rejectsRequiredQuantityLessThanOrEqualToOne() {
        assertThrows(IllegalArgumentException.class, () ->
                new MultiBuyOffer(
                        OfferType.MULTI_BUY,
                        "Buy 2 apples for €0.45",
                        validityPeriod,
                        1,
                        Money.eur("0.45")
                )
        );

        assertThrows(IllegalArgumentException.class, () ->
                new MultiBuyOffer(
                        OfferType.MULTI_BUY,
                        "Buy 2 apples for €0.45",
                        validityPeriod,
                        0,
                        Money.eur("0.45")
                )
        );
    }

    @Test
    void rejectsNullBundlePrice() {
        assertThrows(NullPointerException.class, () ->
                new MultiBuyOffer(
                        OfferType.MULTI_BUY,
                        "Buy 2 apples for €0.45",
                        validityPeriod,
                        2,
                        null
                )
        );
    }

    @Test
    void rejectsNonPositiveBundlePrice() {
        assertThrows(IllegalArgumentException.class, () ->
                new MultiBuyOffer(
                        OfferType.MULTI_BUY,
                        "Buy 2 apples for €0.45",
                        validityPeriod,
                        2,
                        Money.zero()
                )
        );
    }

    @Test
    void rejectsNegativeQuantity() {
        MultiBuyOffer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                validityPeriod,
                2,
                Money.eur("0.45")
        );

        assertThrows(IllegalArgumentException.class,
                () -> offer.priceFor(-1, Money.eur("0.30")));
    }

    @Test
    void rejectsNullUnitPrice() {
        MultiBuyOffer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                validityPeriod,
                2,
                Money.eur("0.45")
        );

        assertThrows(NullPointerException.class,
                () -> offer.priceFor(2, null));
    }

    @Test
    void returnsAppliedOfferSummaryFromOfferMetadata() {
        MultiBuyOffer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                validityPeriod,
                2,
                Money.eur("0.45")
        );

        AppliedOfferSummary summary = offer.toAppliedOfferSummary();

        assertEquals("MULTI_BUY", summary.type());
        assertEquals("Buy 2 apples for €0.45", summary.description());
    }
}