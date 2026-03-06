package com.haiilo.checkout.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Simple money value object for EUR amounts.
 *
 * Invariant: values are stored with scale=2 and {@code RoundingMode.HALF_UP}.
 * Avoids floating point errors by using {@link java.math.BigDecimal}.
 */
public final class Money {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    @Getter
    private final BigDecimal amount;
    @Getter
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency) {
        Objects.requireNonNull(amount, "amount must not be null");

        BigDecimal normalized = amount.setScale(SCALE, ROUNDING);

        if (normalized.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }

        this.amount = normalized;
        this.currency = currency;
    }


    public static Money eur(String amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        return new Money(new BigDecimal(amount), Currency.getInstance("EUR"));
    }

    public static Money eur(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("EUR"));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO, Currency.getInstance("EUR"));
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "other must not be null");
        validateCurrency(other);
        return new Money(this.amount.add(other.amount), currency);
    }

    public Money minus(Money other) {
        Objects.requireNonNull(other, "other must not be null");
        validateCurrency(other);

        BigDecimal result = this.amount.subtract(other.amount);
        return new Money(result, this.currency);
    }

    public Money times(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("multiplier must be >= 0");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other);
        return amount.compareTo(other.amount) > 0;
    }

    public Money percentage(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("percentage must be between 0 and 100");
        }

        BigDecimal factor = BigDecimal.valueOf(percentage)
                .divide(BigDecimal.valueOf(100), SCALE + 2, ROUNDING);

        return new Money(this.amount.multiply(factor), this.currency);
    }

    public Money applyPercentageDiscount(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("percentage must be between 0 and 100");
        }

        return this.minus(this.percentage(percentage));
    }

    private void validateCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Money operations require matching currencies");
        }
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money other)) {
            return false;
        }
        return this.amount.compareTo(other.amount) == 0;
    }

    @Override
    public int hashCode() {
        return amount.stripTrailingZeros().hashCode();
    }
}
