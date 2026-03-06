package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Test;

import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoneyTest {

    @Test
    void createsEuroMoney() {
        Money money = Money.eur("1.25");

        assertEquals("1.25", money.toString());
        assertEquals(Currency.getInstance("EUR"), money.getCurrency());
    }

    @Test
    void rejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.eur("-0.01"));
    }

    @Test
    void addsMoneyOfSameCurrency() {
        Money total = Money.eur("0.30").plus(Money.eur("0.20"));

        assertEquals(Money.eur("0.50"), total);
    }

    @Test
    void multipliesAmount() {
        Money total = Money.eur("0.30").times(3);

        assertEquals(Money.eur("0.90"), total);
    }

    @Test
    void rejectsNegativeMultiplier() {
        assertThrows(IllegalArgumentException.class, () -> Money.eur("0.30").times(-1));
    }

    @Test
    void identifiesZero() {
        assertTrue(Money.zero().isZero());
        assertFalse(Money.zero().isPositive());
    }

    @Test
    void identifiesPositiveAmount() {
        assertTrue(Money.eur("0.01").isPositive());
        assertFalse(Money.eur("0.01").isZero());
    }

    @Test
    void appliesPercentageDiscount() {
        Money discounted = Money.eur("1.00").applyPercentageDiscount(10);

        assertEquals(Money.eur("0.90"), discounted);
    }

    @Test
    void rejectsInvalidDiscountPercentage() {
        assertThrows(IllegalArgumentException.class, () -> Money.eur("1.00").applyPercentageDiscount(-1));
        assertThrows(IllegalArgumentException.class, () -> Money.eur("1.00").applyPercentageDiscount(101));
    }
}
