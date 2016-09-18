package com.github.allin.services;

import com.github.allin.models.Client;
import com.github.allin.models.Permission;
import com.github.allin.models.PermissionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 */
@Service
public class AccessTokenServiceImpl implements AccessTokenService{
    @Autowired
    PermissionDAO permissionDAO;
    @Override
    public String getAccessToken(Client client, String userID) {
        Permission permission;
        try {
            permission = permissionDAO.getForClient(client.getClientID(), userID);
        } catch (EmptyResultDataAccessException e) {
            String accessToken = UUID.randomUUID().toString();
            permission = Permission.builder()
                    .accessToken(accessToken)
                    .clientID(client.getClientID())
                    .userID(userID)
                    .build();
            permissionDAO.insert(permission);
        }
        return permission.getAccessToken();
    }
}
