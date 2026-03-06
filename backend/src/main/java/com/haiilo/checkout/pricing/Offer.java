package com.haiilo.checkout.pricing;

import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;

import java.time.LocalDate;

public interface Offer {
    ProductId getProductId();
    boolean isActive(LocalDate date);
    Money priceFor(int quantity, Money unitPrice);
}
