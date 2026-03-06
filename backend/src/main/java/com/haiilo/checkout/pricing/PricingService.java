package com.haiilo.checkout.pricing;

import com.haiilo.checkout.application.OfferCatalog;
import com.haiilo.checkout.domain.CartItem;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.Product;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class PricingService {
    private final OfferCatalog offerCatalog;
    private final Clock clock;

    public PricingService(OfferCatalog offerCatalog) {
        this.offerCatalog = Objects.requireNonNull(offerCatalog, "offerCatalog must not be null");
        this.clock = Clock.systemDefaultZone();
    }

    public Money price(CartItem item, Product product) {
        Objects.requireNonNull(item, "item must not be null");
        Objects.requireNonNull(product, "product must not be null");

        LocalDate today = LocalDate.now(clock);

        return offerCatalog.findActiveOffer(item.productId(), today)
                .map(offer -> offer.priceFor(item.quantity(), product.unitPrice()))
                .orElseGet(() -> product.unitPrice().times(item.quantity()));
    }
}
