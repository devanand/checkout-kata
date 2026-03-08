package com.haiilo.checkout.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "Cart item to be priced")
public record CheckoutItemRequest(
    @Schema(description = "Product identifier", example = "APPLE")
    String productId,
    @Schema(description = "Quantity requested", example = "3")
    @Positive
    int quantity
) {
}
