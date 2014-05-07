package com.zenquery.model.dao.impl;

import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.Query;
import com.zenquery.model.Table;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.util.BasicDataSourceFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by willy on 13.04.14.
 */
public class JdbcDatabaseConnectionDAO implements DatabaseConnectionDAO {
    private Properties databaseDriverProperties;

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private QueryDAO queryDAO;

    private BasicDataSourceFactory dataSourceFactory;

    public void setDatabaseDriverProperties(Properties databaseDriverProperties) {
        this.databaseDriverProperties = databaseDriverProperties;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setQueryDAO(QueryDAO queryDAO) {
        this.queryDAO = queryDAO;
    }

    public void setDataSourceFactory(BasicDataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public DatabaseConnection find(Integer id) {
        String sql = "SELECT * FROM database_connections WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        DatabaseConnection databaseConnection =
                jdbcTemplate.query(sql, new Object[] { id }, new DatabaseConnectionMapper()).get(0);

        return databaseConnection;
    }

    public List<DatabaseConnection> findAll() {
        String sql = "SELECT * FROM database_connections";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<DatabaseConnection> databaseConnections =
                jdbcTemplate.query(sql, new DatabaseConnectionMapper());

        return databaseConnections;
    }

    public Number insert(final DatabaseConnection databaseConnection) {
        final String sql = "INSERT INTO database_connections (name, url, username, password) VALUES (?, ?, ?, ?)";

        jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[] { "id" });

                preparedStatement.setString(1, databaseConnection.getName());
                preparedStatement.setString(2, databaseConnection.getUrl());
                preparedStatement.setString(3, databaseConnection.getUsername());
                preparedStatement.setString(4, databaseConnection.getPassword());

                return preparedStatement;
            }
        };
        jdbcTemplate.update(
                preparedStatementCreator,
                keyHolder
        );

        String selectAllUserTablesSql = databaseDriverProperties.getProperty(databaseConnection.getUrl() + ".queries.selectAllUserTables");

        BasicDataSource dataSource = dataSourceFactory.getBasicDataSource(
                databaseConnection.getUrl(),
                databaseConnection.getUsername(),
                databaseConnection.getPassword()
        );

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Table> tables =
                jdbcTemplate.query(selectAllUserTablesSql, new TableMapper());
        for (Table table : tables) {
            Query query = new Query();
            query.setDatabaseConnectionId(keyHolder.getKey().intValue());
            query.setContent("SELECT * FROM " + table.getName());
            queryDAO.insert(query);
        }

        return keyHolder.getKey();
    }

    public void update(Integer id, DatabaseConnection databaseConnection) {
        String sql = "UPDATE database_connections SET name = ?, url = ?, username = ?, password = ? WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(
                sql,
                new Object[] {
                        databaseConnection.getName(),
                        databaseConnection.getUrl(),
                        databaseConnection.getUsername(),
                        databaseConnection.getPassword(),
                        id
                }
        );
    }

    public void delete(Integer id) {
        queryDAO.deleteByDatabaseConnectionId(id);

        String sql = "DELETE FROM database_connections WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[] { id });
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

    private static class TableMapper implements ParameterizedRowMapper<Table> {
        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
            Table table = new Table();

            table.setName(rs.getString("table_name"));

            return table;
        }
    }
}
