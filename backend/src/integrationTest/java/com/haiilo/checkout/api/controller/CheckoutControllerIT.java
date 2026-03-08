package com.haiilo.checkout.api.controller;

import com.haiilo.checkout.api.dto.CheckoutItemRequest;
import com.haiilo.checkout.api.dto.CheckoutRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void returnsEmptyItemsAndZeroTotalForEmptyCart() throws Exception {
        CheckoutRequest request = new CheckoutRequest(List.of());

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.total").value("0.00"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void appliesMultiBuyOfferDuringCheckout() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(new CheckoutItemRequest("APPLE", 3))
        );

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("APPLE"))
                .andExpect(jsonPath("$.items[0].quantity").value(3))
                .andExpect(jsonPath("$.items[0].unitPrice").value("0.30"))
                .andExpect(jsonPath("$.items[0].lineTotal").value("0.75"))
                .andExpect(jsonPath("$.items[0].appliedOffer.type").value("MULTI_BUY"))
                .andExpect(jsonPath("$.items[0].appliedOffer.description").value("Buy 2 apples for €0.45"))
                .andExpect(jsonPath("$.total").value("0.75"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void appliesPercentDiscountOfferDuringCheckout() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(new CheckoutItemRequest("BANANA", 3))
        );

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("BANANA"))
                .andExpect(jsonPath("$.items[0].quantity").value(3))
                .andExpect(jsonPath("$.items[0].unitPrice").value("0.20"))
                .andExpect(jsonPath("$.items[0].lineTotal").value("0.54"))
                .andExpect(jsonPath("$.items[0].appliedOffer.type").value("PERCENT_DISCOUNT"))
                .andExpect(jsonPath("$.items[0].appliedOffer.description").value("10% discount on bananas"))
                .andExpect(jsonPath("$.total").value("0.54"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returnsBadRequestForUnknownProduct() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(new CheckoutItemRequest("MANGO", 1))
        );

        mockMvc.perform(post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("UNKNOWN_PRODUCT"))
                .andExpect(jsonPath("$.message").value("Unknown product: MANGO"));
    }
}