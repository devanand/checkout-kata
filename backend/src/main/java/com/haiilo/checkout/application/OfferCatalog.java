package com.haiilo.checkout.application;

import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.pricing.Offer;

import java.time.LocalDate;
import java.util.Optional;

public interface OfferCatalog {
    Optional<Offer> findActiveOffer(ProductId productId, LocalDate date);
}
