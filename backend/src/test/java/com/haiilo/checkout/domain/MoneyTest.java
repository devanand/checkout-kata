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
    void addsMoneyCorrectly() {
        Money total = Money.eur("0.30").plus(Money.eur("0.20"));

        assertEquals(Money.eur("0.50"), total);
    }

    @Test
    void subtractsMoneyCorrectly() {
        Money result = Money.eur("1.00").minus(Money.eur("0.25"));

        assertEquals(Money.eur("0.75"), result);
    }

    @Test
    void rejectsSubtractionThatWouldMakeMoneyNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> Money.eur("0.20").minus(Money.eur("0.30")));
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
    void identifiesZeroAmount() {
        Money money = Money.zero();

        assertTrue(money.isZero());
        assertFalse(money.isPositive());
    }

    @Test
    void identifiesPositiveAmount() {
        Money money = Money.eur("0.01");

        assertTrue(money.isPositive());
        assertFalse(money.isZero());
    }

    @Test
    void calculatesZeroPercentage() {
        Money percentage = Money.eur("1.00").percentage(0);

        assertEquals(Money.zero(), percentage);
    }

    @Test
    void calculatesFullPercentage() {
        Money percentage = Money.eur("1.00").percentage(100);

        assertEquals(Money.eur("1.00"), percentage);
    }

    @Test
    void calculatesPercentageCorrectly() {
        Money percentage = Money.eur("2.00").percentage(25);

        assertEquals(Money.eur("0.50"), percentage);
    }

    @Test
    void rejectsNegativePercentage() {
        assertThrows(IllegalArgumentException.class,
                () -> Money.eur("1.00").percentage(-1));
    }

    @Test
    void rejectsPercentageAboveHundred() {
        assertThrows(IllegalArgumentException.class,
                () -> Money.eur("1.00").percentage(101));
    }

    @Test
    void appliesZeroPercentDiscount() {
        Money discounted = Money.eur("1.00").applyPercentageDiscount(0);

        assertEquals(Money.eur("1.00"), discounted);
    }

    @Test
    void appliesHundredPercentDiscount() {
        Money discounted = Money.eur("1.00").applyPercentageDiscount(100);

        assertEquals(Money.zero(), discounted);
    }

    @Test
    void appliesPercentageDiscountCorrectly() {
        Money discounted = Money.eur("1.00").applyPercentageDiscount(10);

        assertEquals(Money.eur("0.90"), discounted);
    }

    @Test
    void rejectsInvalidDiscountPercentage() {
        assertThrows(IllegalArgumentException.class,
                () -> Money.eur("1.00").applyPercentageDiscount(-1));
        assertThrows(IllegalArgumentException.class,
                () -> Money.eur("1.00").applyPercentageDiscount(101));
    }
}
