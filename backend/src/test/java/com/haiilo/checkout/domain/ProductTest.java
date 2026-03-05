package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    void createsProductWithValidPrice() {
        Product product = new Product(ProductId.of("APPLE"), Money.eur("0.30"));
        assertEquals(ProductId.of("APPLE"), product.productId());
        assertEquals(Money.eur("0.30"), product.unitPrice());
    }

    @Test
    void rejectsZeroPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(ProductId.of("APPLE"), Money.zero()));
    }

    @Test
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product(ProductId.of("APPLE"), Money.eur("-1.00")));
    }

    @Test
    void rejectsNullProductId() {
        assertThrows(NullPointerException.class,
                () -> new Product(null, Money.eur("0.30")));
    }

    @Test
    void rejectsNullUnitPrice() {
        assertThrows(NullPointerException.class,
                () -> new Product(ProductId.of("APPLE"), null));
    }
}
