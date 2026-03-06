package com.haiilo.checkout.api.dto;

public record CheckoutResponse(
    String total,
    String currency
) {
}
