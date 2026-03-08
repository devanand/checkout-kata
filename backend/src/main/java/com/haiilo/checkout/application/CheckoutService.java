package com.haiilo.checkout.application;


import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.CartItem;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.exception.UnknownProductException;
import com.haiilo.checkout.pricing.PricingResult;
import com.haiilo.checkout.pricing.PricingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public CheckoutResult checkout(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");

        List<CheckoutLine> lines = new ArrayList<>();
        Money total = Money.zero();

        for (CartItem item : cart.items()) {
            Product product = catalog.findById(item.productId())
                    .orElseThrow(() -> new UnknownProductException(item.productId().value()));

            PricingResult pricingResult = pricingService.price(item, product);

            lines.add(new CheckoutLine(
                    item.productId(),
                    item.quantity(),
                    product.unitPrice(),
                    pricingResult.appliedOffer(),
                    pricingResult.lineTotal()
            ));

            total = total.plus(pricingResult.lineTotal());
        }

        return new CheckoutResult(lines, total);
    }
}
