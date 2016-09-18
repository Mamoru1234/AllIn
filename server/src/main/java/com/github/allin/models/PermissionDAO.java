package com.github.allin.models;

/**
 */
public interface PermissionDAO {
    int insert(Permission permission);
    Permission getForClient(String clientID, String userID);
    Permission getForToken(String token);
}
