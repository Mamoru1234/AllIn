package com.github.allin.models;

/**
 */
public interface UserDAO {
    User getByID(String userID);
    User getByMail(String userMail);
    int insert(User user);

}
