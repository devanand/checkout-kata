package com.haiilo.checkout.infrastructure.persistence.entity;

import com.haiilo.checkout.pricing.OfferType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "offers")
public class OfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OfferType type;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @Column(name = "required_quantity")
    private Integer requiredQuantity;

    @Column(name = "bundle_price", precision = 19, scale = 2)
    private BigDecimal bundlePrice;

    @Column(name = "percentage")
    private Integer percentage;

    protected OfferEntity() {
    }

    public OfferEntity(
            OfferType type,
            LocalDate validFrom,
            LocalDate validUntil,
            Integer requiredQuantity,
            BigDecimal bundlePrice,
            Integer percentage
    ) {
        this.type = type;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.requiredQuantity = requiredQuantity;
        this.bundlePrice = bundlePrice;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public OfferType getType() {
        return type;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public Integer getRequiredQuantity() {
        return requiredQuantity;
    }

    public BigDecimal getBundlePrice() {
        return bundlePrice;
    }

    public Integer getPercentage() {
        return percentage;
    }
}