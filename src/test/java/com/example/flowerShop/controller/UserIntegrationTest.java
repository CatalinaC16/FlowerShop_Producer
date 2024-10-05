package com.example.flowerShop.controller;

import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.utils.user.UserRole;
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
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSignupAndLogin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/createUser")
                        .param("name", "Balkan Girl")
                        .param("email", "girl.balkanfood@gmail.com")
                        .param("password", "password123")
                        .param("contactNumber", "1234567890")
                        .param("address", "123 Main")
                        .param("role", UserRole.ADMIN.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

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

        mockMvc.perform(MockMvcRequestBuilders.post("/update/{id}", userId)
                        .session(session)
                        .param("id", userId.toString())
                        .param("name", "Updated Balkan Girl")
                        .param("email", "girl.balkanfood@gmail.com")
                        .param("contactNumber", "0987654321")
                        .param("address", "456 Elm")
                        .param("role", UserRole.CUSTOMER.toString()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/listOfUsers"));
    }
}