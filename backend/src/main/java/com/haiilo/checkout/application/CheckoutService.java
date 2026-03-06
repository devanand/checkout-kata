package com.haiilo.checkout.application;


import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.CartItem;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import com.haiilo.checkout.exception.UnknownProductException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CheckoutService {
    private final Catalog catalog;

    public CheckoutService(Catalog catalog) {
        this.catalog = catalog;
    }

    public Money checkout(Cart cart) {
        Objects.requireNonNull(cart, "cart must not be null");

        Money total = Money.zero();

        for (CartItem item: cart.items()) {
            Product product = catalog.findById(item.productId())
                    .orElseThrow(() -> new UnknownProductException(item.productId().value()));
            Money lineTotal = product.unitPrice().times(item.quantity());
            total = total.plus(lineTotal);
        }
        return total;
    }
}
