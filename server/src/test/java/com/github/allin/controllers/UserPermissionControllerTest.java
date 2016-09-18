package com.github.allin.controllers;

import com.github.allin.models.Client;
import com.github.allin.models.ClientDAO;
import com.github.allin.models.User;
import com.github.allin.models.UserDAO;
import com.github.allin.services.GrantTokenService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class UserPermissionControllerTest {
    private MockMvc mockMvc;

    @Autowired
    UserPermissionController userPermissionController;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ClientDAO clientDAO;

    @Autowired
    GrantTokenService grantTokenService;

    @Before
    public void setup() {
        mockMvc = MockMVCGenerator.generate(userPermissionController);
        reset(userDAO, clientDAO, grantTokenService);
    }
    @Test
    public void getUserPermissionPage() throws Exception {
        User user = getUserInstance("USer_id");
        Client client = getClientInstance("Client_id");
        when(clientDAO.getByID(client.getClientID())).thenReturn(client);
        when(userDAO.getByID(user.getUserID())).thenReturn(user);
        mockMvc.perform(
            get("/user/permission")
                .sessionAttr("client_id", client.getClientID())
                .sessionAttr("user_id", user.getUserID()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("permission"))
            .andExpect(model().attribute("client", client))
            .andExpect(model().attribute("user", user));
        verify(clientDAO, times(1)).getByID(client.getClientID());
        verify(userDAO, times(1)).getByID(user.getUserID());
        verifyNoMoreInteractions(grantTokenService, clientDAO, userDAO);
    }
    @Test
    public void postPermissionForm() throws Exception {
        String redirectURL = "redirectURL";
        String clientID = "clientID";
        String userID = "userID";
        String grantToken = "grantToken";
        when(grantTokenService.getToken(userID)).thenReturn(grantToken);
        mockMvc.perform(
            post("/user/permission")
                .param("client_id", clientID)
                .param("user_id", userID)
                .sessionAttr("redirect_url", redirectURL))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(redirectURL + "?grant_token=" + grantToken));
        verify(grantTokenService, times(1)).getToken(userID);
        verifyNoMoreInteractions(grantTokenService, userDAO, clientDAO);
    }
    public static User getUserInstance(String id) {
        return User.builder()
                .userID(id)
                .userPassword("Some pass")
                .userMail("Some mail")
                .build();
    }
    public static Client getClientInstance(String id) {
        return  Client.builder()
                .clientID(id)
                .clientName("Some name")
                .clientSecret("Secret")
                .build();
    }
    @Configuration
    static public class Config {
        @Bean
        public UserPermissionController userPermissionController() {
            return new UserPermissionController();
        }
        @Bean
        public ClientDAO clientDAO() {
            return mock(ClientDAO.class);
        }
        @Bean
        public UserDAO userDAO() {
            return mock(UserDAO.class);
        }
        @Bean
        public GrantTokenService grantTokenService() {
            return mock(GrantTokenService.class);
        }
    }
}
