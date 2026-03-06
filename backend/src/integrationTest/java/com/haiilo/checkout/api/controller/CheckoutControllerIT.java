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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void returnsZeroForEmptyCart() throws Exception {
        CheckoutRequest request = new CheckoutRequest(List.of());
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("0.00"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void calculatesTotalForSingleItem() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
            List.of(new CheckoutItemRequest("APPLE", 2))
        );
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("0.60"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void calculatesTotalForMixedItems() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
            List.of(
                new CheckoutItemRequest("APPLE", 2),
                new CheckoutItemRequest("BANANA", 1)
            )
        );
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("0.80"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returnsBadRequestForUnknownProduct() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
            List.of(new CheckoutItemRequest("MANGO", 1))
        );

        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("UNKNOWN_PRODUCT"))
                .andExpect(jsonPath("$.message").value("Unknown product: MANGO"));
    }
}
