package com.haiilo.checkout.infrastructure.offer;

import com.haiilo.checkout.infrastructure.offer.factory.OfferFactoryRegistry;
import com.haiilo.checkout.pricing.Offer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

/**
 * Loads offer configurations from a JSON resource and converts
 * them into Offer domain objects.
 *
 * Offers are read during application startup and later stored
 * in memory for fast lookup during checkout.
 */
@Component
public class OfferLoader {

    private final ObjectMapper objectMapper;
    private final OfferFactoryRegistry offerFactoryRegistry;
    private final Resource offersResource;

    public OfferLoader(
            ObjectMapper objectMapper,
            OfferFactoryRegistry offerFactoryRegistry,
            @Value("classpath:offers.json") Resource offersResource
    ) {
        this.objectMapper = objectMapper;
        this.offerFactoryRegistry = offerFactoryRegistry;
        this.offersResource = offersResource;
    }

    public List<Offer> loadOffers() {
        return readConfiguredOffers().stream()
                .map(offerFactoryRegistry::create)
                .toList();
    }

    private List<OfferConfig> readConfiguredOffers() {
        try (InputStream inputStream = offersResource.getInputStream()) {
            return objectMapper.readValue(inputStream, offerConfigListType());
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load offers from offers.json", ex);
        }
    }

    private JavaType offerConfigListType() {
        return objectMapper.getTypeFactory()
                .constructCollectionType(List.class, OfferConfig.class);
    }
}
