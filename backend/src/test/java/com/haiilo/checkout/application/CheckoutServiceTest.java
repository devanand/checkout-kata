package com.haiilo.checkout.application;

import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.exception.UnknownProductException;
import com.haiilo.checkout.pricing.PricingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {

    private final Catalog catalog = Mockito.mock(Catalog.class);
    private final OfferCatalog offerCatalog = Mockito.mock(OfferCatalog.class);
    private final PricingService pricingService = new PricingService(offerCatalog);
    private final CheckoutService checkoutService = new CheckoutService(catalog, pricingService);

    @Test
    void returnsEmptyResultForEmptyCart() {
        CheckoutResult result = checkoutService.checkout(new Cart());

        assertNotNull(result);
        assertTrue(result.items().isEmpty());
        assertEquals(Money.zero(), result.total());
    }

    @Test
    void calculatesTotalForSingleItemWithoutOffer() {
        Cart cart = new Cart();
        cart.add(ProductId.of("APPLE"), 1);

        Mockito.when(catalog.findById(ProductId.of("APPLE")))
                .thenReturn(Optional.of(
                        new Product(
                                ProductId.of("APPLE"),
                                Money.eur("0.30")
                        )
                ));

        CheckoutResult result = checkoutService.checkout(cart);

        assertEquals(Money.eur("0.30"), result.total());
        assertEquals(1, result.items().size());

        CheckoutLine line = result.items().get(0);
        assertEquals(ProductId.of("APPLE"), line.productId());
        assertEquals(1, line.quantity());
        assertEquals(Money.eur("0.30"), line.unitPrice());
        assertEquals(Money.eur("0.30"), line.lineTotal());
        assertNull(line.appliedOffer());
    }

    @Test
    void throwsForUnknownProduct() {
        Cart cart = new Cart();
        cart.add(ProductId.of("MANGO"), 1);

        Mockito.when(catalog.findById(ProductId.of("MANGO")))
                .thenReturn(Optional.empty());

        assertThrows(UnknownProductException.class, () -> checkoutService.checkout(cart));
    }
}
