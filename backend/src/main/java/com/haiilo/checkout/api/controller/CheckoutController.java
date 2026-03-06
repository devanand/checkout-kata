package com.haiilo.checkout.api.controller;

import com.haiilo.checkout.api.dto.CheckoutItemRequest;
import com.haiilo.checkout.api.dto.CheckoutRequest;
import com.haiilo.checkout.api.dto.CheckoutResponse;
import com.haiilo.checkout.application.CheckoutService;
import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;


    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        Cart cart = toCart(request);
        Money total = checkoutService.checkout(cart);
        return new CheckoutResponse(total.getAmount().toPlainString(), total.getCurrency().getCurrencyCode());
    }

    private Cart toCart(CheckoutRequest request) {
        Cart cart = new Cart();

        for (CheckoutItemRequest item : request.items()) {
            cart.add(ProductId.of(item.productId()), item.quantity());
        }

        return cart;
    }
}
