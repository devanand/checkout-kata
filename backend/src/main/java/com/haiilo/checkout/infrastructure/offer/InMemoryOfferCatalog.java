package com.haiilo.checkout.infrastructure.offer;

import com.haiilo.checkout.application.OfferCatalog;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.pricing.MultiBuyOffer;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.PercentDiscountOffer;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;


import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
