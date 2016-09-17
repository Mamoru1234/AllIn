package com.github.allin.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
@Repository
public class PermissionDAOImpl implements PermissionDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    public int insert(Permission permission) {
        String sql = "INSERT INTO permissions (access_token, client_id, user_id) VALUES (? , ?, ?)";

        return  jdbcTemplate.update(sql,
                permission.getAccessToken(), permission.getClientID(), permission.getUserID());
    }

    private class Mapper implements RowMapper<Permission> {

        @Override
        public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Permission.builder()
                    .accessToken(rs.getString("access_token"))
                    .clientID(rs.getString("client_id"))
                    .userID(rs.getString("user_id"))
                    .build();
        }
    }

}
