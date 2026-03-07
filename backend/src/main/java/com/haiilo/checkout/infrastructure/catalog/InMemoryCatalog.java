package com.haiilo.checkout.infrastructure.catalog;

import com.haiilo.checkout.application.Catalog;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.domain.ProductId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation of the product catalog.
 *
 * Stores a predefined set of products and provides lookup
 * functionality by product identifier.
 */
@Component
public class InMemoryCatalog implements Catalog {
    private final Map<ProductId, Product> products = Map.of(
        ProductId.of("APPLE"), new Product(ProductId.of("APPLE"), Money.eur("0.30")),
        ProductId.of("BANANA"), new Product(ProductId.of("BANANA"), Money.eur("0.20")),
        ProductId.of("ORANGE"), new Product(ProductId.of("ORANGE"), Money.eur("0.50"))
    );

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.ofNullable(products.get(productId));
    }
}
