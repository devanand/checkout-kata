package com.haiilo.checkout.application;

import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.exception.UnknownProductException;
import com.haiilo.checkout.pricing.PricingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void calculatesTotalForSingleItem() {
        Cart cart = new Cart();
        cart.add(ProductId.of("APPLE"), 1);

        Mockito.when(catalog.findById(ProductId.of("APPLE")))
                .thenReturn(java.util.Optional.of(
                        new com.haiilo.checkout.domain.Product(
                                ProductId.of("APPLE"),
                                Money.eur("0.30")
                        )
                ));

        CheckoutResult result = checkoutService.checkout(cart);

        assertEquals(Money.eur("0.30"), result.total());
        assertEquals(1, result.items().size());

        CheckoutLine line = result.items().getFirst();
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
                .thenReturn(java.util.Optional.empty());

        assertThrows(UnknownProductException.class, () -> checkoutService.checkout(cart));
    }
}