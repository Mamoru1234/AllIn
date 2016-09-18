package com.github.allin.services;

import com.github.allin.models.Client;

/**
 */
public interface AccessTokenService {
    String getAccessToken(Client client, String userID);
}
