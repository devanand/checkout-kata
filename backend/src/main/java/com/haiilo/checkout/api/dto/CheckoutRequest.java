package com.haiilo.checkout.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CheckoutRequest(
    @NotNull(message = "items must not be null")
    List<@Valid CheckoutItemRequest> items
) {
}
