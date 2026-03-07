package com.haiilo.checkout.pricing;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidityPeriodTest {
    @Test
    void isActiveOnStartDate() {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertTrue(validityPeriod.isActive(LocalDate.of(2026, 3, 1)));
    }

    @Test
    void isActiveOnEndDate() {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertTrue(validityPeriod.isActive(LocalDate.of(2026, 3, 31)));
    }

    @Test
    void isActiveWithinRange() {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertTrue(validityPeriod.isActive(LocalDate.of(2026, 3, 15)));
    }

    @Test
    void isInactiveBeforeStartDate() {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertFalse(validityPeriod.isActive(LocalDate.of(2026, 2, 28)));
    }

    @Test
    void isInactiveAfterEndDate() {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertFalse(validityPeriod.isActive(LocalDate.of(2026, 4, 1)));
    }

    @Test
    void rejectsRangeWhereEndDateIsBeforeStartDate() {
        assertThrows(IllegalArgumentException.class, () ->
                new ValidityPeriod(
                        LocalDate.of(2026, 3, 31),
                        LocalDate.of(2026, 3, 1)
                )
        );
    }

    @Test
    void rejectsNullStartDate() {
        assertThrows(NullPointerException.class, () ->
                new ValidityPeriod(
                        null,
                        LocalDate.of(2026, 3, 31)
                )
        );
    }

    @Test
    void rejectsNullEndDate() {
        assertThrows(NullPointerException.class, () ->
                new ValidityPeriod(
                        LocalDate.of(2026, 3, 1),
                        null
                )
        );
    }

    @Test
    void rejectsNullDateWhenCheckingActivity() {
        ValidityPeriod validityPeriod = new ValidityPeriod(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        );

        assertThrows(NullPointerException.class, () -> validityPeriod.isActive(null));
    }
}
