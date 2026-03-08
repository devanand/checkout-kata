package com.haiilo.checkout.pricing;

import com.haiilo.checkout.application.OfferCatalog;
import com.haiilo.checkout.domain.CartItem;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.domain.ProductId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PricingServiceTest {

    @Test
    void usesRegularPriceWhenNoOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("APPLE"), 2);
        Product product = new Product(ProductId.of("APPLE"), Money.eur("0.30"));

        when(offerCatalog.findActiveOffer(eq(ProductId.of("APPLE")), eq(LocalDate.now())))
                .thenReturn(Optional.empty());

        PricingResult result = pricingService.price(item, product);

        assertEquals(Money.eur("0.60"), result.lineTotal());
        assertNull(result.appliedOffer());
    }

    @Test
    void usesMultiBuyOfferWhenOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("APPLE"), 3);
        Product product = new Product(ProductId.of("APPLE"), Money.eur("0.30"));
        Offer offer = new MultiBuyOffer(
                OfferType.MULTI_BUY,
                "Buy 2 apples for €0.45",
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.now()),
                2,
                Money.eur("0.45")
        );

        when(offerCatalog.findActiveOffer(eq(ProductId.of("APPLE")), eq(LocalDate.now())))
                .thenReturn(Optional.of(offer));

        PricingResult result = pricingService.price(item, product);

        assertEquals(Money.eur("0.75"), result.lineTotal());
        assertNotNull(result.appliedOffer());
        assertEquals("MULTI_BUY", result.appliedOffer().type());
        assertEquals("Buy 2 apples for €0.45", result.appliedOffer().description());
    }

    @Test
    void usesPercentDiscountOfferWhenOfferExists() {
        OfferCatalog offerCatalog = mock(OfferCatalog.class);
        PricingService pricingService = new PricingService(offerCatalog);

        CartItem item = new CartItem(ProductId.of("BANANA"), 3);
        Product product = new Product(ProductId.of("BANANA"), Money.eur("0.20"));
        Offer offer = new PercentDiscountOffer(
                OfferType.PERCENT_DISCOUNT,
                "10% discount on bananas",
                new ValidityPeriod(LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 31)),
                10
        );

        when(offerCatalog.findActiveOffer(eq(ProductId.of("BANANA")), eq(LocalDate.now())))
                .thenReturn(Optional.of(offer));

        PricingResult result = pricingService.price(item, product);

        assertEquals(Money.eur("0.54"), result.lineTotal());
        assertNotNull(result.appliedOffer());
        assertEquals("PERCENT_DISCOUNT", result.appliedOffer().type());
        assertEquals("10% discount on bananas", result.appliedOffer().description());
    }
}
