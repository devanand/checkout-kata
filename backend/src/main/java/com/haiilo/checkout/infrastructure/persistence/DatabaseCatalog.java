package com.haiilo.checkout.infrastructure.persistence;

import com.haiilo.checkout.application.Catalog;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.infrastructure.persistence.entity.ProductEntity;
import com.haiilo.checkout.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

@Component
@Primary
public class DatabaseCatalog implements Catalog {

    private final ProductJpaRepository productJpaRepository;

    public DatabaseCatalog(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = Objects.requireNonNull(productJpaRepository, "productJpaRepository must not be null");
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        Objects.requireNonNull(productId, "productId must not be null");

        return productJpaRepository.findById(productId.value())
                .map(this::toDomain);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                ProductId.of(entity.getId()),
                Money.of(entity.getPriceAmount(), Currency.getInstance(entity.getCurrency()))
        );
    }
}