package com.haiilo.checkout.infrastructure.offer;

import com.haiilo.checkout.infrastructure.offer.factory.OfferFactoryRegistry;
import com.haiilo.checkout.pricing.Offer;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OfferLoaderTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OfferFactoryRegistry offerFactoryRegistry = mock(OfferFactoryRegistry.class);

    @Test
    void loadsOffersFromJson() {
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

        Resource offersResource = new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8));
        OfferLoader offerLoader = new OfferLoader(objectMapper, offerFactoryRegistry, offersResource);

        when(offerFactoryRegistry.create(any(OfferConfig.class))).thenReturn(offer);

        List<Offer> offers = offerLoader.loadOffers();

        assertEquals(1, offers.size());
        assertSame(offer, offers.get(0));
        verify(offerFactoryRegistry, times(1)).create(any(OfferConfig.class));
    }

    @Test
    void throwsIllegalStateExceptionForMalformedJson() {
        String malformedJson = """
                [
                  {
                    "type": "MULTI_BUY",
                    "productId": "APPLE"
                """;

        Resource offersResource = new ByteArrayResource(malformedJson.getBytes(StandardCharsets.UTF_8));
        OfferLoader offerLoader = new OfferLoader(objectMapper, offerFactoryRegistry, offersResource);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                offerLoader::loadOffers
        );

        assertTrue(exception.getMessage().contains("Failed to load offers"));
    }

    @Test
    void throwsIllegalStateExceptionWhenResourceCannotBeRead() {
        Resource unreadableResource = mock(Resource.class);

        OfferLoader offerLoader = new OfferLoader(objectMapper, offerFactoryRegistry, unreadableResource);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                offerLoader::loadOffers
        );

        assertTrue(exception.getMessage().contains("Failed to load offers"));
    }
}
