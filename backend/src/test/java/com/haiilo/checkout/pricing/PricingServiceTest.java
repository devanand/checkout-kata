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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PricingServiceTest {

    @Test
    void usesRegularPriceWhenNoOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("APPLE"), 2);
        Product product = new Product(ProductId.of("APPLE"), Money.eur("0.30"));

        when(offerCatalog.findActiveOffer(eq(ProductId.of("APPLE")), eq(LocalDate.of(2026, 3, 15))))
                .thenReturn(Optional.empty());

        PricingResult result = pricingService.price(item, product);

        assertEquals(Money.eur("0.60"), result.lineTotal());
        assertNull(result.appliedOffer());
    }

    @Test
    void usesOfferPriceWhenOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("APPLE"), 3);
        Product product = new Product(ProductId.of("APPLE"), Money.eur("0.30"));
        Offer offer = new MultiBuyOffer(
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
                2,
                Money.eur("0.45")
        );

        when(offerCatalog.findActiveOffer(eq(ProductId.of("APPLE")), eq(LocalDate.now())))
                .thenReturn(Optional.of(offer));

        PricingResult result = pricingService.price(item, product);

        assertEquals(Money.eur("0.75"), result.lineTotal());
        assertNotNull(result.appliedOffer());
        assertEquals("MULTI_BUY", result.appliedOffer().type());
    }

    @Test
    void usesPercentDiscountOfferWhenOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("BANANA"), 3);
        Product product = new Product(ProductId.of("BANANA"), Money.eur("0.20"));
        Offer offer = new PercentDiscountOffer(
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
                10
        );

        when(offerCatalog.findActiveOffer(eq(ProductId.of("BANANA")), eq(LocalDate.now())))
                .thenReturn(Optional.of(offer));

        PricingResult result = pricingService.price(item, product);

        assertEquals(Money.eur("0.54"), result.lineTotal());
        assertNotNull(result.appliedOffer());
        assertEquals("PERCENT_DISCOUNT", result.appliedOffer().type());
    }
}