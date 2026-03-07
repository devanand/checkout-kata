package com.haiilo.checkout.application;


import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.CartItem;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.exception.UnknownProductException;
import com.haiilo.checkout.pricing.PricingService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Application service responsible for orchestrating the checkout process.
 *
 * Retrieves product information from the catalog and delegates
 * price calculation to the pricing service.
 */
@Service
public class CheckoutService {
    private final Catalog catalog;
    private final PricingService pricingService;

    public CheckoutService(Catalog catalog, PricingService pricingService) {
        this.catalog = catalog;
        this.pricingService = pricingService;
    }

    public Money checkout(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");

        Money total = Money.zero();

        for (CartItem item: cart.items()) {
            Product product = catalog.findById(item.productId())
                    .orElseThrow(() -> new UnknownProductException(item.productId().value()));
            total = total.plus(pricingService.price(item, product));
        }
        return total;
    }
}
