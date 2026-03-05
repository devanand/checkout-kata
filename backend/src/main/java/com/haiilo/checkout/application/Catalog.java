package com.haiilo.checkout.application;

import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.domain.ProductId;

import java.util.Optional;

/**
 * Provides access to products available for purchase.
 *
 * The catalog acts as the source of truth for product pricing
 * during checkout.
 */
public interface Catalog {
    Optional<Product> findById(ProductId productId);
}
