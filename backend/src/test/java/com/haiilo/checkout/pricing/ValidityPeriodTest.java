package com.haiilo.checkout.pricing;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidityPeriodTest {
    @Test
    void isActiveInsideRange() {
        ValidityPeriod period = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertTrue(period.isActive(LocalDate.of(2026, 3, 15)));
    }

    @Test
    void isInactiveBeforeRange() {
        ValidityPeriod period = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertFalse(period.isActive(LocalDate.of(2026, 2, 28)));
    }

    @Test
    void rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () ->
                new ValidityPeriod(LocalDate.of(2026, 3, 31), LocalDate.of(2026, 3, 1)));
    }
}
