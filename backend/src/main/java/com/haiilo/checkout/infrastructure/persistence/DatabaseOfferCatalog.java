package com.haiilo.checkout.infrastructure.persistence;

import com.haiilo.checkout.application.OfferCatalog;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.infrastructure.persistence.entity.OfferAssignmentEntity;
import com.haiilo.checkout.infrastructure.persistence.entity.OfferEntity;
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

    public DatabaseOfferCatalog(OfferAssignmentJpaRepository offerAssignmentJpaRepository) {
        this.offerAssignmentJpaRepository = Objects.requireNonNull(
                offerAssignmentJpaRepository,
                "offerAssignmentJpaRepository must not be null"
        );
    }

    @Override
    public Optional<Offer> findActiveOffer(ProductId productId, LocalDate date) {
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(date, "date must not be null");

        return offerAssignmentJpaRepository.findByProduct_IdOrderByPriorityAsc(productId.value()).stream()
                .map(OfferAssignmentEntity::getOffer)
                .map(this::toDomain)
                .filter(offer -> offer.isActive(date))
                .findFirst();
    }

    private Offer toDomain(OfferEntity entity) {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                entity.getValidFrom(),
                entity.getValidUntil()
        );

        if (entity.getType() == OfferType.MULTI_BUY) {
            return new MultiBuyOffer(
                    validityPeriod,
                    entity.getRequiredQuantity(),
                    Money.of(entity.getBundlePrice(), Currency.getInstance("EUR"))
            );
        }

        if (entity.getType() == OfferType.PERCENT_DISCOUNT) {
            return new PercentDiscountOffer(
                    validityPeriod,
                    entity.getPercentage()
            );
        }

        throw new IllegalArgumentException("Unsupported offer type: " + entity.getType());
    }
}