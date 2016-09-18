package com.github.allin.models;

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
public class GeneralInfoDAO implements PublicDAO<GeneralInfo>{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int insert(String userID, GeneralInfo value) {
        String sql = "INSERT INTO personal_info(user_id, first_name, sur_name, gender, country) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, userID, value.getFirstName(),
                value.getSurName(), value.getGender().toString(), value.getCountry());
    }

    @Override
    public int update(String userID, GeneralInfo value) {
        String sql = "UPDATE LOW_PRIORITY personal_info SET first_name=?, sur_name=?, gender=?, country=? WHERE user_id=?";
        return jdbcTemplate.update(sql, value.getFirstName(), value.getSurName(),
                value.getGender().toString(), value.getCountry(), userID);
    }

    @Override
    public GeneralInfo getByID(String userID) {
        String sql = "SELECT * FROM personal_info WHERE user_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userID}, new Mapper());
    }

    @Override
    public List<GeneralInfo> getListByID(String userID) {
        throw new UnsupportedOperationException();
    }
    public static class Mapper implements RowMapper<GeneralInfo> {

        @Override
        public GeneralInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return GeneralInfo.builder()
                    .firstName(rs.getString("first_name"))
                    .surName(rs.getString("sur_name"))
                    .gender(GeneralInfo.Gender.valueOf(rs.getString("gender")))
                    .country(rs.getString("country"))
                    .build();
        }
    }
}
