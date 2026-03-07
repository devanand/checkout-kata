package com.haiilo.checkout.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Checkout request containing cart items")
public record CheckoutRequest(
    @NotNull(message = "items must not be null")
    List<@Valid CheckoutItemRequest> items
) {
}
