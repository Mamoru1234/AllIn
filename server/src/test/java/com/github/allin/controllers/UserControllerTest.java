package com.github.allin.controllers;

import com.github.allin.models.User;
import com.github.allin.models.UserDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import utils.MockMVCGenerator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mockMvc = MockMVCGenerator.generate(userController);
    }
    @After
    public void after() {
        verifyNoMoreInteractions(userDAO);
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
                .andExpect(view().name("redirect:/user/permission"));
        verify(userDAO).getByMail(user.getUserMail());
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
    }
    @Test
    public void signUpGetPage() throws Exception {
        mockMvc.perform(get("/user/sign_up"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("sign_up"));
    }
    @Test
    public void signUpPost() throws Exception {
        String mail = "Some mail";
        String pass = "Secret pass";
        mockMvc.perform(
            post("/user/sign_up")
                .param("user_mail", mail)
                .param("user_pass", pass))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/"));
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userDAO, times(1)).insert(argument.capture());
        assertEquals("Correct user mail", mail, argument.getValue().getUserMail());
        assertEquals("Correct user pass", pass, argument.getValue().getUserPassword());
    }
    @Test
    public void signUpPostWrongMail() throws Exception {
        String mail = "Some mail";
        String pass = "Secret pass";
        when(userDAO.insert(any(User.class))).thenThrow(new DataIntegrityViolationException(""));
        mockMvc.perform(
                post("/user/sign_up")
                        .param("user_mail", mail)
                        .param("user_pass", pass))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("sign_up"))
                .andExpect(model().attribute("err_mail", true));
        verify(userDAO, times(1)).insert(any(User.class));
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
