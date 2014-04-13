package com.zenquery.model.dao.impl;

import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.dao.DatabaseConnectioDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by willy on 13.04.14.
 */
public class JdbcDatabaseConnectionDAO implements DatabaseConnectioDAO {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DatabaseConnection find(Integer id){
        String sql = "SELECT * FROM database_connections WHERE id=?";

        jdbcTemplate = new JdbcTemplate(dataSource);

        DatabaseConnection databaseConnection =
                jdbcTemplate.query(sql, new Object[] { id }, new DatabaseConnectionMapper()).get(0);

        return databaseConnection;
    }

    private static class DatabaseConnectionMapper implements ParameterizedRowMapper<DatabaseConnection> {
        public DatabaseConnection mapRow(ResultSet rs, int rowNum) throws SQLException {
            DatabaseConnection databaseConnection = new DatabaseConnection();

            databaseConnection.setId(rs.getInt("id"));
            databaseConnection.setName(rs.getString("name"));
            databaseConnection.setUrl(rs.getString("url"));
            databaseConnection.setUsername(rs.getString("username"));
            databaseConnection.setPassword(rs.getString("password"));

            return databaseConnection;
        }
    }
}
