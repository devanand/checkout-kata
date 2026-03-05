package com.haiilo.checkout.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Represents a customer's shopping cart.
 *
 * The cart stores grouped {@link CartItem}s rather than individual
 * product entries. Quantities are aggregated when the same product is
 * added multiple times.
 */
public final class Cart {
    private final List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void add(ProductId productId, int quantity) {
        Objects.requireNonNull(productId, "productId must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }

        Optional<CartItem> existingItem = findItem(productId);

        if (existingItem.isPresent()) {
            CartItem updatedItem = existingItem.get().increaseBy(quantity);
            replace(existingItem.get(), updatedItem);
            return;
        }

        items.add(new CartItem(productId, quantity));
    }

    public Optional<CartItem> findItem(ProductId productId) {
        Objects.requireNonNull(productId, "productId must not be null");
        return items.stream()
                .filter(item -> item.productId().equals(productId))
                .findFirst();
    }

    public List<CartItem> items() {
        return List.copyOf(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private void replace(CartItem existingItem, CartItem updatedItem) {
        int index = items.indexOf(existingItem);
        items.set(index, updatedItem);
    }
}
