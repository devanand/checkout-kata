package com.haiilo.checkout.infrastructure.offer.factory;

import com.haiilo.checkout.infrastructure.offer.OfferConfig;
import com.haiilo.checkout.pricing.Offer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OfferFactoryRegistry {
    private final List<OfferFactory> factories;

    public OfferFactoryRegistry(List<OfferFactory> factories) {
        this.factories = List.copyOf(Objects.requireNonNull(factories, "factories must not be null"));
    }

    public Offer create(OfferConfig config) {
        return factories.stream()
                .filter(factory -> factory.supports(config.type()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unsupported offer type: " + config.type()))
                .create(config);
    }
}
