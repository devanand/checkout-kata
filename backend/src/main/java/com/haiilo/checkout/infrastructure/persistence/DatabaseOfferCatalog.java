package com.haiilo.checkout.infrastructure.persistence;

import com.haiilo.checkout.application.OfferCatalog;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.infrastructure.persistence.entity.OfferAssignmentEntity;
import com.haiilo.checkout.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.infrastructure.persistence.mapper.OfferEntityMapperRegistry;
import com.haiilo.checkout.infrastructure.persistence.repository.OfferAssignmentJpaRepository;
import com.haiilo.checkout.pricing.MultiBuyOffer;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.OfferType;
import com.haiilo.checkout.pricing.PercentDiscountOffer;
import com.haiilo.checkout.pricing.ValidityPeriod;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

@Component
@Primary
public class DatabaseOfferCatalog implements OfferCatalog {

    private final OfferAssignmentJpaRepository offerAssignmentJpaRepository;
    private final OfferEntityMapperRegistry offerEntityMapperRegistry;

    public DatabaseOfferCatalog(OfferAssignmentJpaRepository offerAssignmentJpaRepository, OfferEntityMapperRegistry offerEntityMapperRegistry) {
        this.offerAssignmentJpaRepository = Objects.requireNonNull(
                offerAssignmentJpaRepository,
                "offerAssignmentJpaRepository must not be null"
        );
        this.offerEntityMapperRegistry = Objects.requireNonNull(
                offerEntityMapperRegistry,
                "offerEntityMapperRegistry must not be null"
        );
    }

    @Override
    public Optional<Offer> findActiveOffer(ProductId productId, LocalDate date) {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(date, "date must not be null");

        return offerAssignmentJpaRepository.findByProduct_IdOrderByPriorityAsc(productId.value()).stream()
                .map(OfferAssignmentEntity::getOffer)
                .map(offerEntityMapperRegistry::toDomain)
                .filter(offer -> offer.isActive(date))
                .findFirst();
    }

    
}
