package com.haiilo.checkout.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Priced checkout line item")
public record CheckoutItemResponse(
        @Schema(description = "Product identifier", example = "APPLE")
        String productId,
        @Schema(description = "Requested quantity", example = "3")
        int quantity,
        @Schema(description = "Unit price before discount", example = "0.30")
        String unitPrice,
        @Schema(description = "Final line total after pricing rules are applied", example = "0.75")
        String lineTotal,
        @Schema(description = "Offer information for the item, if present")
        AppliedOfferResponse appliedOffer
) {
}
