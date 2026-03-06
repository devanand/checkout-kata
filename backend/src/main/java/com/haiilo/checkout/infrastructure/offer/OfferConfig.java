package com.haiilo.checkout.infrastructure.offer;

public record OfferConfig(String type,
                          String productId,
                          String validFrom,
                          String validUntil,
                          Integer requiredQuantity,
                          String bundlePrice,
                          Integer percentage) {
}
