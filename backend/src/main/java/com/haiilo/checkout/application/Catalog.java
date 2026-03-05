package com.haiilo.checkout.application;

import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.domain.ProductId;

import java.util.Optional;

public interface Catalog {
    Optional<Product> findById(ProductId productId);
}
