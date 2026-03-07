package com.haiilo.checkout.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Checkout response with calculated total price")
public record CheckoutResponse(
    String total,
    String currency
) {
}
