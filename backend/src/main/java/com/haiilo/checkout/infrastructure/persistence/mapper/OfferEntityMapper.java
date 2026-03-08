package com.haiilo.checkout.infrastructure.persistence.mapper;

import com.haiilo.checkout.infrastructure.persistence.entity.OfferEntity;
import com.haiilo.checkout.pricing.Offer;
import com.haiilo.checkout.pricing.OfferType;

public interface OfferEntityMapper {

    OfferType supportedType();

    Offer toDomain(OfferEntity entity);
}