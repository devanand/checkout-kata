package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CartItemTest {

    @Test
    void createsCartItemWithValidValues() {
        CartItem cartItem = new CartItem(ProductId.of("APPLE"), 2);

        assertEquals(ProductId.of("APPLE"), cartItem.productId());
        assertEquals(2, cartItem.quantity());
    }

    @Test
    void rejectsNullProductId() {
        assertThrows(NullPointerException.class, () -> new CartItem(null, 1));
    }

    @Test
    void rejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(ProductId.of("APPLE"), 0));
        assertThrows(IllegalArgumentException.class, () -> new CartItem(ProductId.of("APPLE"), -1));
    }

    @Test
    void increasesQuantity() {
        CartItem cartItem = new CartItem(ProductId.of("APPLE"), 2);

        CartItem updated = cartItem.increaseBy(3);

        assertEquals(ProductId.of("APPLE"), updated.productId());
        assertEquals(5, updated.quantity());
    }

    @Test
    void rejectsNonPositiveIncreaseQuantity() {
        CartItem cartItem = new CartItem(ProductId.of("APPLE"), 2);

        assertThrows(IllegalArgumentException.class, () -> cartItem.increaseBy(0));
        assertThrows(IllegalArgumentException.class, () -> cartItem.increaseBy(-1));
    }
}
