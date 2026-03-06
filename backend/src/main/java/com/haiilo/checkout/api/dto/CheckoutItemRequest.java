package com.haiilo.checkout.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CheckoutItemRequest(
    @NotBlank(message = "productId must not be blank")
    String productId,

    @Positive
    int quantity
) {
}
