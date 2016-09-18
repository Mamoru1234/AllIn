package com.github.allin.models;

import java.util.List;

/**
 */
public interface PublicDAO<T> {
    int insert(String userID, T value);
    int update(String userID, T value);
    T getByID(String userID);
    List<T> getListByID(String userID);
}
