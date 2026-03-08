package com.haiilo.checkout.infrastructure.persistence.mapper;

import com.haiilo.checkout.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.OfferType;
import com.haiilo.checkout.pricing.PercentDiscountOffer;
import com.haiilo.checkout.pricing.ValidityPeriod;
import org.springframework.stereotype.Component;

@Component
public class PercentDiscountOfferEntityMapper implements OfferEntityMapper {

    @Override
    public OfferType supportedType() {
        return OfferType.PERCENT_DISCOUNT;
    }

    @Override
    public Offer toDomain(OfferEntity entity) {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                entity.getValidFrom(),
                entity.getValidUntil()
        );

        return new PercentDiscountOffer(
                entity.getType(),
                entity.getDescription(),
                validityPeriod,
                entity.getPercentage()
        );
    }
}