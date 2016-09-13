package com.github.allin.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
@Builder
@Data
public class User {
    private String userMail;
    private String userPassword;
    private String userID;

    @Repository
    public static class DAO {
        @Autowired
        JdbcTemplate jdbcTemplate;

        public User getByMail(String userMail) {
            String sql = "SELECT * FROM users WHERE user_mail = ?";
            return jdbcTemplate.queryForObject(sql, new Object[] {userMail}, new Mapper());
        }
        public int insert(User user) {
            String sql = "INSERT INTO users (user_id, user_mail, user_password) VALUES (? , ?, ?)";
            return jdbcTemplate.update(sql, new Object[]{user.getUserID(), user.getUserMail(), user.getUserPassword()});
        }
        class Mapper implements RowMapper<User>{

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return User.builder()
                        .userID(rs.getString("user_id"))
                        .userMail(rs.getString("user_mail"))
                        .userPassword(rs.getString("user_password"))
                        .build();
            }
        }
    }
}
