package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartTest {
    
    @Test
    void startsEmpty() {
        Cart cart = new Cart();

        assertTrue(cart.isEmpty());
        assertTrue(cart.items().isEmpty());
    }

    @Test
    void addsNewItemToCart() {
        Cart cart = new Cart();

        cart.add(ProductId.of("APPLE"), 2);

        assertFalse(cart.isEmpty());
        assertEquals(1, cart.items().size());
        assertEquals(2, cart.findItem(ProductId.of("APPLE")).orElseThrow().quantity());
    }

    @Test
    void increasesQuantityWhenProductAlreadyExists() {
        Cart cart = new Cart();

        cart.add(ProductId.of("APPLE"), 2);
        cart.add(ProductId.of("APPLE"), 3);

        assertEquals(1, cart.items().size());
        assertEquals(5, cart.findItem(ProductId.of("APPLE")).orElseThrow().quantity());
    }

    @Test
    void findsExistingItem() {
        Cart cart = new Cart();
        cart.add(ProductId.of("APPLE"), 2);

        assertTrue(cart.findItem(ProductId.of("APPLE")).isPresent());
    }

    @Test
    void returnsEmptyWhenItemDoesNotExist() {
        Cart cart = new Cart();

        assertTrue(cart.findItem(ProductId.of("APPLE")).isEmpty());
    }

    @Test
    void rejectsNullProductId() {
        Cart cart = new Cart();

        assertThrows(NullPointerException.class, () -> cart.add(null, 1));
        assertThrows(NullPointerException.class, () -> cart.findItem(null));
    }

    @Test
    void rejectsNonPositiveQuantity() {
        Cart cart = new Cart();

        assertThrows(IllegalArgumentException.class, () -> cart.add(ProductId.of("APPLE"), 0));
        assertThrows(IllegalArgumentException.class, () -> cart.add(ProductId.of("APPLE"), -1));
    }

    @Test
    void returnsUnmodifiableItemsView() {
        Cart cart = new Cart();
        cart.add(ProductId.of("APPLE"), 1);

        assertThrows(UnsupportedOperationException.class,
                () -> cart.items().add(new CartItem(ProductId.of("BANANA"), 1)));
    }
}
