package com.github.allin.models;

import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
@Log4j
@Builder
@Data
public class User {
    private String userMail;
    private String userPassword;
    private String userID;

    @Repository
    public static class DAO {

    }
}
