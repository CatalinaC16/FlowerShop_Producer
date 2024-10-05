package com.example.flowerShop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login/user")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "girl.balkanfood@gmail.com")
                        .param("password", "password123"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/createCart"))
                .andReturn();

        mockMvc.perform(MockMvcRequestBuilders.post("/product/createProduct")
                        .param("id", String.valueOf(UUID.randomUUID()))
                        .param("name", "Zambila roz")
                        .param("description", "Foarte dragute cu un miros special")
                        .param("imageUrl", "/images/zambila.jpg")
                        .param("price", String.valueOf(20.0))
                        .param("category", "FLOWERS")
                        .param("stock", String.valueOf(100)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/product/listOfProducts"));
    }
}
