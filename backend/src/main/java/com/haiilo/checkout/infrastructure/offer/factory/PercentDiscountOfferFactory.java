package com.haiilo.checkout.infrastructure.offer.factory;

import com.haiilo.checkout.domain.ProductId;
import com.haiilo.checkout.infrastructure.offer.OfferConfig;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.PercentDiscountOffer;
import com.haiilo.checkout.pricing.ValidityPeriod;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PercentDiscountOfferFactory implements OfferFactory {

    private static final String TYPE = "PERCENT_DISCOUNT";

    @Override
    public boolean supports(String type) {
        return TYPE.equals(type);
    }

    @Override
    public Offer create(OfferConfig config) {
        return new PercentDiscountOffer(
                ProductId.of(config.productId()),
                new ValidityPeriod(LocalDate.parse(config.validFrom()), LocalDate.parse(config.validUntil())),
                config.percentage()
        );
    }
}
