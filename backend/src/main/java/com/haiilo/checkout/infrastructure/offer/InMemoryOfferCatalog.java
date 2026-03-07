package com.haiilo.checkout.infrastructure.offer;

import com.haiilo.checkout.application.OfferCatalog;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.pricing.Offer;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * In-memory store of offers loaded at application startup.
 *
 * Provides lookup functionality to retrieve the currently
 * active offer for a product.
 */
@Component
public class InMemoryOfferCatalog implements OfferCatalog {
    private final List<Offer> offers;

    public InMemoryOfferCatalog(OfferLoader offerLoader) {
        this.offers = List.copyOf(offerLoader.loadOffers());
    }

    @Override
    public Optional<Offer> findActiveOffer(ProductId productId, LocalDate date) {
        return offers.stream()
                .filter(offer -> offer.getProductId().equals(productId))
                .filter(offer -> offer.isActive(date))
                .findFirst();
    }
}
