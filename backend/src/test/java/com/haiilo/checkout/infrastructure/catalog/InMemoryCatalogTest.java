package com.haiilo.checkout.infrastructure.catalog;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryCatalogTest {
    @Test
    void findsKnownProduct() {
        InMemoryCatalog catalog = new InMemoryCatalog();

        var productOpt = catalog.findById(ProductId.of("apple"));

        assertTrue(productOpt.isPresent());
        assertEquals(ProductId.of("APPLE"), productOpt.get().productId());
        assertEquals(Money.eur("0.30"), productOpt.get().unitPrice());
    }

    @Test
    void returnsEmptyForUnknownProduct() {
        InMemoryCatalog catalog = new InMemoryCatalog();
        var productOpt = catalog.findById(ProductId.of("UNKNOWN"));
        assertTrue(productOpt.isEmpty());
    }
}
