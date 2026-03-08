package com.haiilo.checkout.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Offer information shown for a checkout item")

public record AppliedOfferResponse(
        @Schema(description = "Offer type", example = "MULTI_BUY")
        String type,
        @Schema(description = "Human-readable offer description", example = "Buy 2 apples for 0.45")
        String description
) {
}
