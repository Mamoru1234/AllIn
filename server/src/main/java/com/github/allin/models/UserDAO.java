package com.github.allin.models;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 */
public interface UserDAO {
    User getByID(String userID);
    User getByMail(String userMail);
    int insert(User user);

}
