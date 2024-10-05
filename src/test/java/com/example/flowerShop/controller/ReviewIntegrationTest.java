package com.example.flowerShop.controller;

import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddReview() throws Exception {
        MvcResult loginResult = mockMvc.perform(MockMvcRequestBuilders.post("/login/user")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "girl.balkanfood@gmail.com")
                        .param("password", "password123"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/createCart"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession();
        UserGetDTO loggedInUser = (UserGetDTO) session.getAttribute("loggedInUser");
        UUID userId = loggedInUser.getId();

        ReviewDetailedDTO review = new ReviewDetailedDTO();
        review.setId(UUID.randomUUID());
        review.setId_product(UUID.fromString("89dbd964-cda8-44b7-af9c-a3bc87a4e960"));
        review.setId_user(userId);
        review.setText("Foarte dragute primite de aniversare!");
        review.setRating(10);

        mockMvc.perform(MockMvcRequestBuilders.post("/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/review/listOfReviews"));
    }
}
