package com.github.allin.controllers;

import com.github.allin.models.User;
import com.github.allin.models.UserDAO;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.ViewResolverGenerator;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    UserController userController;
    @Autowired
    UserDAO userDAO;

    @Before
    public void setUp() {
        reset(userDAO);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setViewResolvers(ViewResolverGenerator.generate())
                .build();
    }
    @Test
    public void signInGet() throws Exception {
        mockMvc.perform(get("/user/sign_in"))
                .andExpect(status().is2xxSuccessful());
    }
    @Test
    public void signInPost() throws Exception {
        User user = User.builder()
                .userID("userID")
                .userMail("userMail")
                .userPassword("userPassword")
                .build();
        when(userDAO.getByMail(user.getUserMail())).thenReturn(user);
        mockMvc.perform(
            post("/user/sign_in")
                .param("user_mail", user.getUserMail())
                .param("user_password", user.getUserPassword()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/permission"));
    }
    @Configuration
    public static class Config {
        @Bean
        public UserController userController() {
            return new UserController();
        }
        @Bean
        public UserDAO userDAO() {
            return mock(UserDAO.class);
        }
    }
}
