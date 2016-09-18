package com.github.allin.models;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 */
@Repository
public class ClientDAOImpl implements ClientDAO{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Client getByID(String clientID) {
        String sql = "SELECT * FROM clients WHERE client_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clientID}, new Mapper());
    }

    @Override
    public List<Client> getAll() {
        return jdbcTemplate.query("SELECT * FROM clients", new Mapper());
    }

    private class Mapper implements RowMapper<Client> {

        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Client.builder()
                    .clientID(rs.getString("client_id"))
                    .clientName(rs.getString("client_name"))
                    .clientSecret(rs.getString("client_secret"))
                    .build();
        }
    }
}
