package com.haiilo.checkout.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Simple money value object for EUR amounts.
 *
 * <p>Invariant: values are stored with scale=2 and {@code RoundingMode.HALF_UP}.
 * Avoids floating point errors by using {@link java.math.BigDecimal}.</p>
 */
public final class Money {
    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount must not be null");

        BigDecimal normalized = amount.setScale(SCALE, ROUNDING);

        if (normalized.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Money amount cannot be negative");
        }

        this.amount = normalized;
    }

    public static Money eur(String amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        return new Money(new BigDecimal(amount));
    }

    public static Money eur(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money plus(Money other) {
        Objects.requireNonNull(other, "other must not be null");
        return new Money(this.amount.add(other.amount));
    }

    public Money times(int multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("multiplier must be >= 0");
        }
        return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
    }

    public BigDecimal asBigDecimal() {
        return amount;
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
