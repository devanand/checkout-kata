package com.haiilo.checkout.api.controller;

import com.haiilo.checkout.api.dto.CheckoutItemRequest;
import com.haiilo.checkout.api.dto.CheckoutRequest;
import com.haiilo.checkout.api.dto.CheckoutResponse;
import com.haiilo.checkout.application.CheckoutService;
import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.Money;
import com.haiilo.checkout.domain.ProductId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing checkout operations.
 *
 * Accepts cart requests, delegates processing to the checkout
 * service, and returns the calculated total price.
 */

@Tag(
        name = "Checkout API",
        description = "Operations related to supermarket checkout pricing"
)
@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;


    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Operation(
            summary = "Checkout cart",
            description = "Calculates the total price of items in a cart and applies applicable offers automatically"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Checkout successful",
        content = @Content(
            schema = @Schema(implementation = CheckoutResponse.class),
            examples = @ExampleObject(
                value = """
                    {
                      "total": "0.75",
                      "currency": "EUR"
                    }
                    """
            )
        )
    )
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
    @PostMapping
    public CheckoutResponse checkout(@Valid
         @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
         @RequestBody CheckoutRequest request) {
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
