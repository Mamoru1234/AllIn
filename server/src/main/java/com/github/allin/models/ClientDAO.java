package com.github.allin.models;

import java.util.List;

/**
 */
public interface ClientDAO{
    Client getByID(String clientID);
    List<Client> getAll();
}
