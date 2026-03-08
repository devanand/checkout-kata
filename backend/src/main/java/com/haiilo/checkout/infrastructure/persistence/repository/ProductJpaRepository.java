package com.haiilo.checkout.infrastructure.persistence.repository;

import com.haiilo.checkout.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, String> {}
