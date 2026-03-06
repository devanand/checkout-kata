package com.haiilo.checkout.infrastructure.offer;

import com.haiilo.checkout.infrastructure.offer.factory.OfferFactoryRegistry;
import com.haiilo.checkout.pricing.Offer;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OfferLoaderTest {

    @Test
    void loadsOffersFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        OfferFactoryRegistry registry = mock(OfferFactoryRegistry.class);
        Offer offer = mock(Offer.class);

        String json = """
                [
                  {
                    "type": "MULTI_BUY",
                    "productId": "APPLE",
                    "validFrom": "2026-03-01",
                    "validUntil": "2026-03-31",
                    "requiredQuantity": 2,
                    "bundlePrice": "0.45"
                  }
                ]
                """;

        ByteArrayResource resource = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
        OfferLoader loader = new OfferLoader(objectMapper, registry, resource);

        when(registry.create(any())).thenReturn(offer);

        List<Offer> offers = loader.loadOffers();

        assertEquals(1, offers.size());
        assertEquals(offer, offers.get(0));
    }
}
