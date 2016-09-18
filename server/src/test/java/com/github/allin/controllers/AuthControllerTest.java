package com.github.allin.controllers;

import com.github.allin.models.Client;
import com.github.allin.models.ClientDAO;
import com.github.allin.services.AccessTokenService;
import com.github.allin.services.GrantTokenService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import utils.MockMVCGenerator;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    GrantTokenService grantTokenService;

    @Autowired
    ClientDAO clientDAO;

    @Autowired
    AccessTokenService accessTokenService;

    @Before
    public void setUp() {
        reset(grantTokenService, accessTokenService, clientDAO);
        mockMvc = MockMVCGenerator.generate(authController);
    }
    @After
    public void after() {
        verifyNoMoreInteractions(grantTokenService, accessTokenService, clientDAO);
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
    @Test
    public void postAccessToken() throws Exception {
        Client client = getClientInstance();
        String grantToken = "token_grant";
        String userID = "userID";
        String accessToken = "accessToken";
        when(clientDAO.getByID(client.getClientID())).thenReturn(client);
        when(grantTokenService.getUserID(grantToken)).thenReturn(userID);
        when(accessTokenService.getAccessToken(client, userID)).thenReturn(accessToken);
        mockMvc.perform(
            post("/oauth/token")
                .param("client_id", client.getClientID())
                .param("client_secret", client.getClientSecret())
                .param("grant_token", grantToken))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.access_token").value(accessToken));
        verify(clientDAO, times(1)).getByID(client.getClientID());
        verify(grantTokenService, times(1)).getUserID(grantToken);
        verify(accessTokenService, times(1)).getAccessToken(client, userID);
    }
    @Test
    public void testClientNotFount() throws Exception {
        String clientID = "Some another id";
        Client client = getClientInstance();
        String grantToken = "some token";
        when(clientDAO.getByID(clientID)).thenThrow(new EmptyResultDataAccessException(0));
        mockMvc.perform(
            post("/oauth/token")
                .param("client_id", clientID)
                .param("client_secret", client.getClientSecret())
                .param("grant_token", grantToken))
            .andExpect(status().is4xxClientError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.reason").value("Wrong client id or secret"));
        verify(clientDAO, times(1)).getByID(clientID);
        verifyNoMoreInteractions(clientDAO, grantTokenService, accessTokenService);
    }
    @Test
    public void testWrongSecret() throws Exception {
        Client client = getClientInstance();
        String grantToken = "some token";
        when(clientDAO.getByID(client.getClientID())).thenReturn(client);
        mockMvc.perform(
                post("/oauth/token")
                        .param("client_id", client.getClientID())
                        .param("client_secret", "wrong secret")
                        .param("grant_token", grantToken))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.reason").value("Wrong client id or secret"));
        verify(clientDAO, times(1)).getByID(client.getClientID());
    }
    @Test
    public void wrongTokenTest() throws Exception {
        Client client = getClientInstance();
        String grantToken = "some token";
        when(clientDAO.getByID(client.getClientID())).thenReturn(client);
        when(grantTokenService.getUserID(grantToken)).thenReturn(null);
        mockMvc.perform(
                post("/oauth/token")
                        .param("client_id", client.getClientID())
                        .param("client_secret", client.getClientSecret())
                        .param("grant_token", grantToken))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.reason").value("Wrong grant_token"));
        verify(clientDAO, times(1)).getByID(client.getClientID());
        verify(grantTokenService, times(1)).getUserID(grantToken);
    }
    public static Client getClientInstance() {
        return Client.builder()
                .clientID("some_id")
                .clientName("My app")
                .clientSecret("secret")
                .build();
    }
    @Configuration
    static class Config {
        @Bean
        public AuthController authController() {
            return new AuthController();
        }

        @Bean
        public GrantTokenService grantTokenService() {
            return mock(GrantTokenService.class);
        }
        @Bean
        public ClientDAO clientDAO() {
            return mock(ClientDAO.class);
        }
        @Bean
        public AccessTokenService permissionDAO() {
            return mock(AccessTokenService.class);
        }
    }
}
