package com.haiilo.checkout.infrastructure.offer;

import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.pricing.MultiBuyOffer;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.ValidityPeriod;
import com.haiilo.checkout.domain.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InMemoryOfferCatalogTest {

    @Test
    void findsActiveOffer() {
        Offer offer = new MultiBuyOffer(
                ProductId.of("APPLE"),
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
                2,
                Money.eur("0.45")
        );

        OfferLoader loader = mock(OfferLoader.class);
        when(loader.loadOffers()).thenReturn(List.of(offer));

        InMemoryOfferCatalog catalog = new InMemoryOfferCatalog(loader);

        assertTrue(catalog.findActiveOffer(ProductId.of("APPLE"), LocalDate.of(2026, 3, 10)).isPresent());
    }

    @Test
    void returnsEmptyWhenNoActiveOfferExists() {
        Offer offer = new MultiBuyOffer(
                ProductId.of("APPLE"),
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
                2,
                Money.eur("0.45")
        );

        OfferLoader loader = mock(OfferLoader.class);
        when(loader.loadOffers()).thenReturn(List.of(offer));

        InMemoryOfferCatalog catalog = new InMemoryOfferCatalog(loader);

        assertTrue(catalog.findActiveOffer(ProductId.of("APPLE"), LocalDate.of(2026, 4, 1)).isEmpty());
    }
}
