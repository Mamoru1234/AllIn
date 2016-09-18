package com.github.allin.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import utils.MockMVCGenerator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class AuthControllerTest {
    private MockMvc mockMvc;
    @Autowired
    AuthController authController;

    @Before
    public void setUp() {
        mockMvc = MockMVCGenerator.generate(authController);
    }
    @Test
    public void authorize_With_Attr() throws Exception {
        String clientID = "testClient";
        String redirectURL = "redirectURL";
        String userID = "redirectURL";
        mockMvc.perform(
            get("/oauth/authorize")
                .param("client_id", clientID)
                .param("redirect_url", redirectURL))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/user/sign_in"))
            .andExpect(model().attribute("client_id", clientID))
            .andExpect(model().attribute("redirect_url", redirectURL));
        mockMvc.perform(
            get("/oauth/authorize")
                .param("client_id", clientID)
                .param("redirect_url", redirectURL)
                .sessionAttr("user_id", userID))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/user/permission"))
            .andExpect(model().attribute("client_id", clientID))
            .andExpect(model().attribute("redirect_url", redirectURL));
    }
    @Test
    public void authorize_Without_Some_Attr() throws Exception {
        String clientID = "testClient";
        String redirectURL = "redirectURL";
        mockMvc.perform(
            get("/oauth/authorize")
                    .param("client_id", clientID))
            .andExpect(status().is4xxClientError());
        mockMvc.perform(
                get("/oauth/authorize")
                        .param("redirect_url", redirectURL))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/oauth/authorize"))
                .andExpect(status().is4xxClientError());
    }
    @Configuration
    static class Config {
        @Bean
        public AuthController authController() {
            return new AuthController();
        }
    }
}
