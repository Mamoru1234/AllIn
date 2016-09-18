package com.github.allin.services;

import com.github.allin.models.Client;
import com.github.allin.models.Permission;
import com.github.allin.models.PermissionDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AccessTokenServiceTest {
    @Autowired
    PermissionDAO permissionDAO;

    @Autowired
    AccessTokenService accessTokenService;

    @Before
    public void setup() {
        reset(permissionDAO);
    }
    @Test
    public void testAccessTokenCreation() {
        String clientID = "ClientID";
        Client client = spy(Client.builder()
                .clientID(clientID)
                .clientName("clientName")
                .clientSecret("ClientSecret")
                .build());
        String userID = "userID";
        when(permissionDAO.getForClient(clientID, userID)).thenThrow(new EmptyResultDataAccessException(0));
        accessTokenService.getAccessToken(client, userID);
        verify(client, times(2)).getClientID();
        ArgumentCaptor<Permission> argument = ArgumentCaptor.forClass(Permission.class);
        verify(permissionDAO, times(1)).getForClient(clientID, userID);
        verify(permissionDAO, times(1)).insert(argument.capture());
        assertEquals("Correct permission userID", argument.getValue().getUserID(), userID);
        assertEquals("Correct permission clientID", argument.getValue().getClientID(), clientID);
        verifyNoMoreInteractions(client, permissionDAO);
    }

    @Configuration
    static class Config {
        @Bean
        public AccessTokenService accessTokenService() {
            return new AccessTokenServiceImpl();
        }
        @Bean
        public PermissionDAO permissionDAO() {
            return mock(PermissionDAO.class);
        }
    }
}
