package com.github.allin.services;

/**
 */
public interface GrantTokenService {
    String getToken(String userID);
    String getUserID(String grantToken);
    void invalidate(String grantToken);
}
