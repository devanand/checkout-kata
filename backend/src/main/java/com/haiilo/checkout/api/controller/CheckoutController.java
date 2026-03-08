package com.haiilo.checkout.api.controller;

import com.haiilo.checkout.api.dto.AppliedOfferResponse;
import com.haiilo.checkout.api.dto.CheckoutItemRequest;
import com.haiilo.checkout.api.dto.CheckoutItemResponse;
import com.haiilo.checkout.api.dto.CheckoutRequest;
import com.haiilo.checkout.api.dto.CheckoutResponse;
import com.haiilo.checkout.application.AppliedOfferSummary;
import com.haiilo.checkout.application.CheckoutLine;
import com.haiilo.checkout.application.CheckoutResult;
import com.haiilo.checkout.application.CheckoutService;
import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.ProductId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Tag(
        name = "Checkout API",
        description = "Operations related to supermarket checkout pricing"
)
@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = Objects.requireNonNull(checkoutService, "checkoutService must not be null");
    }

    @Operation(
            summary = "Checkout cart",
            description = "Calculates the total price of items and applies applicable offers"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Checkout successful",
                    content = @Content(
                            schema = @Schema(implementation = CheckoutResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "items": [
                                                {
                                                  "productId": "APPLE",
                                                  "quantity": 3,
                                                  "unitPrice": "0.30",
                                                  "lineTotal": "0.75",
                                                  "appliedOffer": {
                                                    "type": "MULTI_BUY",
                                                    "description": "Multi-buy offer applied"
                                                  }
                                                }
                                              ],
                                              "total": "0.75",
                                              "currency": "EUR"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or unknown product",
                    content = @Content(
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "error": "UNKNOWN_PRODUCT",
                                              "message": "Unknown product: MANGO"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public CheckoutResponse checkout(
            @RequestBody(
                    description = "Cart items to checkout",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Sample Cart",
                                    value = """
                                            {
                                              "items": [
                                                {
                                                  "productId": "APPLE",
                                                  "quantity": 3
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
            @org.springframework.web.bind.annotation.RequestBody CheckoutRequest request
    ) {
        Cart cart = toCart(request);
        CheckoutResult result = checkoutService.checkout(cart);
        return toResponse(result);
    }

    private Cart toCart(CheckoutRequest request) {
        Cart cart = new Cart();

        for (CheckoutItemRequest item : request.items()) {
            cart.add(ProductId.of(item.productId()), item.quantity());
        }

        return cart;
    }

    private CheckoutResponse toResponse(CheckoutResult result) {
        List<CheckoutItemResponse> items = result.items().stream()
                .map(this::toItemResponse)
                .toList();

        return new CheckoutResponse(
                items,
                result.total().toString(),
                result.total().getCurrency().getCurrencyCode()
        );
    }

    private CheckoutItemResponse toItemResponse(CheckoutLine line) {
        return new CheckoutItemResponse(
                line.productId().value(),
                line.quantity(),
                line.unitPrice().toString(),
                line.lineTotal().toString(),
                toAppliedOfferResponse(line.appliedOffer())
        );
    }

    private AppliedOfferResponse toAppliedOfferResponse(AppliedOfferSummary appliedOffer) {
        if (appliedOffer == null) {
            return null;
        }

        return new AppliedOfferResponse(
                appliedOffer.type(),
                appliedOffer.description()
        );
    }
}