package com.haiilo.checkout.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Checkout result containing priced items and overall total")
public record CheckoutResponse(
    @Schema(description = "Priced items in the cart")
    List<CheckoutItemResponse> items,
    @Schema(description = "Overall checkout total", example = "1.29")
    String total,
    @Schema(description = "Currency code", example = "EUR")
    String currency
) {
}
