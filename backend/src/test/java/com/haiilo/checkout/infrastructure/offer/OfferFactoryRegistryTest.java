package com.haiilo.checkout.infrastructure.offer.factory;

import com.haiilo.checkout.infrastructure.offer.OfferConfig;
import com.haiilo.checkout.pricing.Offer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OfferFactoryRegistryTest {

    @Test
    void delegatesToSupportingFactory() {
        OfferFactory factory = mock(OfferFactory.class);
        Offer offer = mock(Offer.class);
        OfferConfig config = new OfferConfig(
                "MULTI_BUY", "APPLE", "2026-03-01", "2026-03-31", 2, "0.45", null
        );

        when(factory.supports("MULTI_BUY")).thenReturn(true);
        when(factory.create(config)).thenReturn(offer);

        OfferFactoryRegistry registry = new OfferFactoryRegistry(List.of(factory));

        assertEquals(offer, registry.create(config));
    }

    @Test
    void throwsForUnsupportedType() {
        OfferFactory factory = mock(OfferFactory.class);
        OfferConfig config = new OfferConfig(
                "UNKNOWN", "APPLE", "2026-03-01", "2026-03-31", null, null, null
        );

        when(factory.supports("UNKNOWN")).thenReturn(false);

        OfferFactoryRegistry registry = new OfferFactoryRegistry(List.of(factory));

        assertThrows(IllegalArgumentException.class, () -> registry.create(config));
    }
}
