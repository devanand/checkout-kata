package com.haiilo.checkout.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Item to checkout")
public record CheckoutItemRequest(
    @NotBlank(message = "productId must not be blank")
    String productId,
    @Positive
    int quantity
) {
}
