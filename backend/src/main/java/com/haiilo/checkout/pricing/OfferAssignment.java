package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.ProductId;

import java.util.Objects;

public record OfferAssignment(ProductId productId, Offer offer) {
    public OfferAssignment {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(offer, "offer must not be null");
    }
}
