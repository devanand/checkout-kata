package com.haiilo.checkout.pricing;

import com.haiilo.checkout.application.OfferCatalog;
import com.haiilo.checkout.domain.CartItem;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.domain.ProductId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PricingServiceTest {

    @Test
    void usesRegularPriceWhenNoOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("APPLE"), 2);
        Product product = new Product(ProductId.of("APPLE"), Money.eur("0.30"));

        when(offerCatalog.findActiveOffer(eq(ProductId.of("APPLE")), eq(LocalDate.of(2026, 3, 15))))
                .thenReturn(Optional.empty());

        assertEquals(Money.eur("0.60"), pricingService.price(item, product));
    }

    @Test
    void usesOfferPriceWhenOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("APPLE"), 3);
        Product product = new Product(ProductId.of("APPLE"), Money.eur("0.30"));
        Offer offer = new MultiBuyOffer(
                ProductId.of("APPLE"),
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
                2,
                Money.eur("0.45")
        );

        when(offerCatalog.findActiveOffer(eq(ProductId.of("APPLE")), eq(LocalDate.now())))
                .thenReturn(Optional.of(offer));

        assertEquals(Money.eur("0.75"), pricingService.price(item, product));
    }
}
