package com.haiilo.checkout.infrastructure.persistence.mapper;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.pricing.MultiBuyOffer;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.OfferType;
import com.haiilo.checkout.pricing.ValidityPeriod;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class MultiBuyOfferEntityMapper implements OfferEntityMapper {

    @Override
    public OfferType supportedType() {
        return OfferType.MULTI_BUY;
    }

    @Override
    public Offer toDomain(OfferEntity entity) {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                entity.getValidFrom(),
                entity.getValidUntil()
        );

        return new MultiBuyOffer(
                entity.getType(),
                entity.getDescription(),
                validityPeriod,
                entity.getRequiredQuantity(),
                Money.of(entity.getBundlePrice(), Currency.getInstance("EUR"))
        );
    }
}
