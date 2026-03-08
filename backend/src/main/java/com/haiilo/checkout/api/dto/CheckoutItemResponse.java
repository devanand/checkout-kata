package com.haiilo.checkout.api.dto;

public record CheckoutItemResponse(
        String productId,
        int quantity,
        String unitPrice,
        String lineTotal,
        AppliedOfferResponse appliedOffer
) {
}
