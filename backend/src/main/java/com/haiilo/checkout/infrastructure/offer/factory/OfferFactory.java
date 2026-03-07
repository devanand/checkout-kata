package com.haiilo.checkout.infrastructure.offer.factory;

import com.haiilo.checkout.infrastructure.offer.OfferConfig;
import com.haiilo.checkout.pricing.Offer;

/**
 * Factory interface responsible for creating Offer instances
 * from configuration objects.
 *
 * Implementations support specific offer types.
 */
public interface OfferFactory {
    boolean supports(String type);
    Offer create(OfferConfig config);
}
