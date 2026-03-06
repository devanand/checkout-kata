package com.haiilo.checkout.infrastructure.offer.factory;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.infrastructure.offer.OfferConfig;
import com.haiilo.checkout.pricing.MultiBuyOffer;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.ValidityPeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MultiBuyOfferFactory implements OfferFactory {
    private static final String TYPE = "MULTI_BUY";

    @Override
    public boolean supports(String type) {
        return TYPE.equals(type);
    }

    @Override
    public Offer create(OfferConfig config) {
        return new MultiBuyOffer(
                ProductId.of(config.productId()),
                new ValidityPeriod(LocalDate.parse(config.validFrom()), LocalDate.parse(config.validUntil())),
                config.requiredQuantity(),
                Money.eur(config.bundlePrice())
        );
    }
}
