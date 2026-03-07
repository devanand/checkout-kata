package com.haiilo.checkout.infrastructure.offer.factory;

import com.haiilo.checkout.infrastructure.offer.OfferConfig;
import com.haiilo.checkout.pricing.Offer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfferFactoryRegistryTest {

    @Test
    void delegatesToSupportingFactory() {
        OfferFactory multiBuyFactory = mock(OfferFactory.class);
        Offer offer = mock(Offer.class);

        OfferConfig config = new OfferConfig(
                "MULTI_BUY",
                "APPLE",
                "2026-03-01",
                "2026-03-31",
                2,
                "0.45",
                null
        );

        when(multiBuyFactory.supports("MULTI_BUY")).thenReturn(true);
        when(multiBuyFactory.create(config)).thenReturn(offer);

        OfferFactoryRegistry registry = new OfferFactoryRegistry(List.of(multiBuyFactory));

        Offer createdOffer = registry.create(config);

        assertSame(offer, createdOffer);
        verify(multiBuyFactory).supports("MULTI_BUY");
        verify(multiBuyFactory).create(config);
    }

    @Test
    void throwsWhenNoFactorySupportsOfferType() {
        OfferFactory multiBuyFactory = mock(OfferFactory.class);

        OfferConfig config = new OfferConfig(
                "UNKNOWN_TYPE",
                "APPLE",
                "2026-03-01",
                "2026-03-31",
                null,
                null,
                null
        );

        when(multiBuyFactory.supports("UNKNOWN_TYPE")).thenReturn(false);

        OfferFactoryRegistry registry = new OfferFactoryRegistry(List.of(multiBuyFactory));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> registry.create(config)
        );

        assertTrue(exception.getMessage().contains("Unsupported offer type"));
        verify(multiBuyFactory).supports("UNKNOWN_TYPE");
        verify(multiBuyFactory, never()).create(any());
    }
}
