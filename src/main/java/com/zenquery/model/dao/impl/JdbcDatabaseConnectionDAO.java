package com.zenquery.model.dao.impl;

import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import com.zenquery.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public class JdbcDatabaseConnectionDAO implements DatabaseConnectionDAO {
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Cacheable("sql.databaseConnections")
    public DatabaseConnection find(Integer id) {
        String sql = "SELECT * FROM database_connections WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        DatabaseConnection databaseConnection =
                jdbcTemplate.query(sql, new Object[] { id }, new DatabaseConnectionMapper()).get(0);

        return databaseConnection;
    }

    @Cacheable("sql.databaseConnections")
    public List<DatabaseConnection> findAll() {
        String sql = "SELECT * FROM database_connections";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<DatabaseConnection> databaseConnections =
                jdbcTemplate.query(sql, new DatabaseConnectionMapper());

        return databaseConnections;
    }

    public Number insert(DatabaseConnection databaseConnection) {
        String sql = "INSERT INTO database_connections (name, url, username, password) VALUES (?, ?, ?, ?)";

        databaseConnection.setPassword(StringUtil.hashWithSha256(databaseConnection.getPassword()));

        jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, new Object[] {
                databaseConnection.getName(),
                databaseConnection.getUrl(),
                databaseConnection.getUsername(),
                databaseConnection.getPassword()
        }, keyHolder);

        return keyHolder.getKey();
    }

    public void update(Integer id, DatabaseConnection databaseConnection) {
        String sql = "UPDATE database_connections SET name = ?, url = ?, username = ?, password = ? WHERE id = ?";

        databaseConnection.setPassword(StringUtil.hashWithSha256(databaseConnection.getPassword()));

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[]{
                databaseConnection.getName(),
                databaseConnection.getUrl(),
                databaseConnection.getUsername(),
                databaseConnection.getPassword(),
                id
        });
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM database_connections WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql);
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
