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
        try (InputStream inputStream = offersResource.getInputStream()) {
            JavaType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, OfferConfig.class);

            List<OfferConfig> configs = objectMapper.readValue(inputStream, listType);

            return configs.stream()
                    .map(offerFactoryRegistry::create)
                    .toList();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to load offers from offers.json", ex);
        }
    }
}
