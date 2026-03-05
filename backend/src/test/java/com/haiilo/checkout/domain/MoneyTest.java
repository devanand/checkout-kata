package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoneyTest {

    @Test
    void allowsZeroAmount() {
        Money money = Money.zero();

        assertEquals(Money.eur("0.00"), money);
        assertTrue(money.isZero());
        assertFalse(money.isPositive());
    }

    @Test
    void rejectsNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.eur("-0.01"));
    }

    @Test
    void addsMoneyCorrectly() {
        Money first = Money.eur("0.30");
        Money second = Money.eur("0.20");

        Money result = first.plus(second);

        assertEquals(Money.eur("0.50"), result);
        assertTrue(result.isPositive());
        assertFalse(result.isZero());
    }

    @Test
    void multipliesMoneyCorrectly() {
        Money unitPrice = Money.eur("0.30");

        Money result = unitPrice.times(3);

        assertEquals(Money.eur("0.90"), result);
    }

    @Test
    void rejectsNegativeMultiplier() {
        assertThrows(IllegalArgumentException.class, () -> Money.eur("1.00").times(-1));
    }

    @Test
    void roundsHalfUpToTwoDecimalPlaces() {
        Money money = Money.eur("0.105");

        assertEquals(Money.eur("0.11"), money);
    }

    @Test
    void identifiesPositiveAmount() {
        Money money = Money.eur("0.30");

        assertTrue(money.isPositive());
        assertFalse(money.isZero());
    }

    @Test
    void identifiesZeroAmount() {
        Money money = Money.eur("0.00");

        assertTrue(money.isZero());
        assertFalse(money.isPositive());
    }

    @Test
    void comparesGreaterAmounts() {
        Money smaller = Money.eur("0.30");
        Money larger = Money.eur("0.50");

        assertTrue(larger.isGreaterThan(smaller));
        assertFalse(smaller.isGreaterThan(larger));
    }

    @Test
    void doesNotTreatEqualAmountsAsGreater() {
        Money first = Money.eur("0.50");
        Money second = Money.eur("0.50");

        assertFalse(first.isGreaterThan(second));
    }
}
