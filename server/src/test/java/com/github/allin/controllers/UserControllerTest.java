package com.github.allin.controllers;

import com.github.allin.models.User;
import com.github.allin.models.UserDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.ViewResolverGenerator;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("sign_in"));
    }
    @Test
    public void signInPost() throws Exception {
        User user = getUserInstance();
        when(userDAO.getByMail(user.getUserMail())).thenReturn(user);
        mockMvc.perform(
            post("/user/sign_in")
                .param("user_mail", user.getUserMail())
                .param("user_password", user.getUserPassword()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(model().attribute("user_id", user.getUserID()))
            .andExpect(view().name("welcome"));
        verify(userDAO).getByMail(user.getUserMail());
        verifyNoMoreInteractions(userDAO);
        reset(userDAO);
        when(userDAO.getByMail(user.getUserMail())).thenReturn(user);
        mockMvc.perform(
                post("/user/sign_in")
                        .param("user_mail", user.getUserMail())
                        .sessionAttr("client_id", "someID")
                        .param("user_password", user.getUserPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("user_id", user.getUserID()))
                .andExpect(view().name("redirect:/permission"));
        verify(userDAO).getByMail(user.getUserMail());
        verifyNoMoreInteractions(userDAO);
    }
    @Test
    public void signInPostWrongMail() throws Exception {
        User user = getUserInstance();
        String wrongEmail = "another_mail";
        when(userDAO.getByMail(wrongEmail)).thenThrow(new EmptyResultDataAccessException(0));
        mockMvc.perform(
                post("/user/sign_in")
                        .param("user_mail", wrongEmail)
                        .param("user_password", user.getUserPassword()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeDoesNotExist("user_id"))
                .andExpect(model().attribute("wrong_input", true))
                .andExpect(view().name("sign_in"));
        verify(userDAO).getByMail(wrongEmail);
        verifyNoMoreInteractions(userDAO);
    }
    @Test
    public void signInPostWrongPass() throws Exception {
        User user = getUserInstance();
        when(userDAO.getByMail(user.getUserMail())).thenReturn(user);
        mockMvc.perform(
                post("/user/sign_in")
                        .param("user_mail", user.getUserMail())
                        .param("user_password", "wrong pass"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeDoesNotExist("user_id"))
                .andExpect(model().attribute("wrong_input", true))
                .andExpect(view().name("sign_in"));
        verify(userDAO).getByMail(user.getUserMail());
        verifyNoMoreInteractions(userDAO);
    }
    private static User getUserInstance() {
        return User.builder()
                .userID("userID")
                .userMail("userMail")
                .userPassword("userPassword")
                .build();
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
