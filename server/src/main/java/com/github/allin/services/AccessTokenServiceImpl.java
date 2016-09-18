package com.github.allin.services;

import com.github.allin.models.Client;
import com.github.allin.models.Permission;
import com.github.allin.models.PermissionDAO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 */
@Log4j
@Service
public class AccessTokenServiceImpl implements AccessTokenService{
    @Autowired
    PermissionDAO permissionDAO;
    @Override
    public String getAccessToken(Client client, String userID) {
        Permission permission;
        try {
            permission = permissionDAO.getForClient(client.getClientID(), userID);
            log.debug("Permission from DB:" + permission);
        } catch (EmptyResultDataAccessException e) {
            String accessToken = UUID.randomUUID().toString();
            permission = Permission.builder()
                    .accessToken(accessToken)
                    .clientID(client.getClientID())
                    .userID(userID)
                    .build();
            permissionDAO.insert(permission);
            log.debug("Permission created: " + permission);
        }
        return permission.getAccessToken();
    }

    @Override
    public String getClientID(String accessToken) {
        return permissionDAO.getForToken(accessToken).getClientID();
    }

}
