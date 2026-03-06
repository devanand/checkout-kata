package com.haiilo.checkout.pricing;

import java.time.LocalDate;
import java.util.Objects;

public final class ValidityPeriod {

    private final LocalDate validFrom;
    private final LocalDate validUntil;

    public ValidityPeriod(LocalDate validFrom, LocalDate validUntil) {
        this.validFrom = Objects.requireNonNull(validFrom, "validFrom must not be null");
        this.validUntil = Objects.requireNonNull(validUntil, "validUntil must not be null");

        if (validUntil.isBefore(validFrom)) {
            throw new IllegalArgumentException("validUntil must not be before validFrom");
        }
    }

    public boolean isActive(LocalDate date) {
        Objects.requireNonNull(date, "date must not be null");
        return !date.isBefore(validFrom) && !date.isAfter(validUntil);
    }

    public LocalDate validFrom() {
        return validFrom;
    }

    public LocalDate validUntil() {
        return validUntil;
    }
}
