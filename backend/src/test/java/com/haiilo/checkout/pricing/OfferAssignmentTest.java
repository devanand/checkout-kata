package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class OfferAssignmentTest {

    private final Offer offer = new MultiBuyOffer(
            OfferType.MULTI_BUY,
            "Buy 2 apples for €0.45",
            new ValidityPeriod(
                    LocalDate.of(2026, 3, 1),
                    LocalDate.of(2026, 3, 31)
            ),
            2,
            Money.eur("0.45")
    );

    @Test
    void createsOfferAssignment() {
        OfferAssignment assignment = new OfferAssignment(ProductId.of("APPLE"), offer);

        assertEquals(ProductId.of("APPLE"), assignment.productId());
        assertSame(offer, assignment.offer());
    }

    @Test
    void rejectsNullProductId() {
        assertThrows(NullPointerException.class, () -> new OfferAssignment(null, offer));
    }

    @Test
    void rejectsNullOffer() {
        assertThrows(NullPointerException.class,
                () -> new OfferAssignment(ProductId.of("APPLE"), null));
    }
}
