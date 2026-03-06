package com.haiilo.checkout.infrastructure.offer.factory;

import com.haiilo.checkout.infrastructure.offer.OfferConfig;
import com.haiilo.checkout.pricing.Offer;

public interface OfferFactory {
    boolean supports(String type);
    Offer create(OfferConfig config);
}
