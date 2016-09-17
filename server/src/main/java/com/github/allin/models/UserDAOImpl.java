package com.github.allin.models;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 */
@Log4j
@Repository
public class UserDAOImpl implements UserDAO{
    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getByID(String userID) {
        log.debug("getByID" + userID);
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] {userID}, new Mapper());
    }

    public User getByMail(String userMail) {
        log.debug("getByMail" + userMail);
        String sql = "SELECT * FROM users WHERE user_mail = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userMail}, new Mapper());
    }
    public int insert(User user) {
        String sql = "INSERT INTO users (user_id, user_mail, user_password) VALUES (? , ?, ?)";
        return jdbcTemplate.update(sql, user.getUserID(), user.getUserMail(),
                user.getUserPassword());
    }
    class Mapper implements RowMapper<User> {

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
