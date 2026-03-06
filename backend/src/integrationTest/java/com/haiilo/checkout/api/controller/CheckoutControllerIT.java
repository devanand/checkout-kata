package com.haiilo.checkout.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CheckoutControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsZeroForEmptyCart() throws Exception {
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("0.00"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void calculatesTotalForSingleItem() throws Exception {
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    {
                                      "productId": "APPLE",
                                      "quantity": 2
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("0.60"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void calculatesTotalForMixedItems() throws Exception {
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "items": [
                                    {
                                      "productId": "APPLE",
                                      "quantity": 2
                                    },
                                    {
                                      "productId": "BANANA",
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value("0.80"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void returnsBadRequestForUnknownProduct() throws Exception {
        mockMvc.perform(post("/api/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "items": [
                                {
                                  "productId": "MANGO",
                                  "quantity": 1
                                }
                              ]
                            }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("UNKNOWN_PRODUCT"))
                .andExpect(jsonPath("$.message").value("Unknown product: MANGO"));
    }
}
