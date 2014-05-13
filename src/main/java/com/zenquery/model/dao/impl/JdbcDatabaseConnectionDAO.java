package com.zenquery.model.dao.impl;

import com.zenquery.model.*;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.util.BasicDataSourceFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by willy on 13.04.14.
 */
public class JdbcDatabaseConnectionDAO implements DatabaseConnectionDAO {
    private static final Logger logger = Logger.getLogger(JdbcDatabaseConnectionDAO.class);

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

        BasicDataSource dataSource = dataSourceFactory.getBasicDataSource(
                databaseConnection.getUrl(),
                databaseConnection.getUsername(),
                databaseConnection.getPassword()
        );

        Map<String, Long> singleEntityQueries = new HashMap<String, Long>();
        Map<String, String> tablePrimaryKeys = new HashMap<String, String>();
        Map<String, List<ForeignKey>> tableForeignKeys = new HashMap<String, List<ForeignKey>>();
        Map<String, String> tableReferences = new HashMap<String, String>();

        String selectAllUserTablesSql = databaseDriverProperties.getProperty(dataSource.getDriverClassName() + ".queries.selectAllUserTables");
        jdbcTemplate = new JdbcTemplate(dataSource);

        List<Table> tables =
                jdbcTemplate.query(selectAllUserTablesSql, new TableMapper());

        try {
            for (Table table : tables) {
                String tableName = table.getName();

                String selectPrimaryKeyForTable = databaseDriverProperties.getProperty(dataSource.getDriverClassName() + ".queries.selectPrimaryKeyForTable");
                jdbcTemplate = new JdbcTemplate(dataSource);
                List<PrimaryKey> primaryKeys = jdbcTemplate.query(selectPrimaryKeyForTable, new Object[] { tableName }, new PrimaryKeyMapper());

                if (primaryKeys.size() > 0) {
                    String primaryKeyColumnName = jdbcTemplate.query(selectPrimaryKeyForTable, new Object[] { tableName }, new PrimaryKeyMapper()).get(0).getColumnName();

                    tablePrimaryKeys.put(tableName, primaryKeyColumnName);

                    Query queryForSingleEntity = new Query();
                    queryForSingleEntity.setDatabaseConnectionId(keyHolder.getKey().intValue());
                    queryForSingleEntity.setContent("SELECT * FROM " + tableName + " WHERE " + primaryKeyColumnName + " = ?");
                    Number tableQueryId = queryDAO.insert(queryForSingleEntity);

                    singleEntityQueries.put(tableName, tableQueryId.longValue());
                }

                String selectAllForeignKeysForTable = databaseDriverProperties.getProperty(dataSource.getDriverClassName() + ".queries.selectAllForeignKeysForTable");
                jdbcTemplate = new JdbcTemplate(dataSource);

                tableForeignKeys.put(tableName, jdbcTemplate.query(selectAllForeignKeysForTable, new Object[] { tableName }, new ForeignKeyMapper()));
            }

            for (String tableName : tableForeignKeys.keySet()) {
                List<ForeignKey> foreignKeys = tableForeignKeys.get(tableName);
                String tableReference = "";

                if (foreignKeys.size() > 0) {
                    for (ForeignKey foreignKey : foreignKeys) {
                        Long queryId = singleEntityQueries.get(foreignKey.getTargetTable());
                        String resultSetForQueryUrl = "/api/v1/resultSetForQuery/" + queryId;

                        tableReference += ", '" + resultSetForQueryUrl + "/' "
                                + databaseDriverProperties.getProperty(dataSource.getDriverClassName() + ".queries.concatOperator")
                                + " " + foreignKey.getSourceKey() + " " + foreignKey.getTargetTable() + "_entity";
                    }
                }

                tableReferences.put(tableName, tableReference);
            }
        } catch (Exception e) {
            logger.debug(e);
        }

        for (Table table : tables) {
            String tableName = table.getName();
            String tableReference = tableReferences.get(tableName) != null ? tableReferences.get(tableName) : "";
            String primaryKeyColumnName = tablePrimaryKeys.get(tableName);

            Query query = new Query();
            query.setDatabaseConnectionId(keyHolder.getKey().intValue());
            query.setContent("SELECT " + tableName + ".*" + tableReference + " FROM " + tableName);
            queryDAO.insert(query);

            try {
                Number tableQueryId = singleEntityQueries.get(tableName);
                if (tableQueryId != null) {
                    Query queryForSingleEntity = queryDAO.find(tableQueryId.intValue());
                    queryForSingleEntity.setContent("SELECT " + tableName + ".*" + tableReference + " FROM " + tableName + " WHERE " + primaryKeyColumnName + " = ?");
                    queryDAO.update(singleEntityQueries.get(tableName).intValue(), queryForSingleEntity);
                }
            } catch (Exception e) {
                logger.debug(e);
            }
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

    private static class ForeignKeyMapper implements ParameterizedRowMapper<ForeignKey> {
        public ForeignKey mapRow(ResultSet rs, int rowNum) throws SQLException {
            ForeignKey foreignKey = new ForeignKey();

            foreignKey.setSourceTable(rs.getString("source_table"));
            foreignKey.setSourceKey(rs.getString("source_key"));
            foreignKey.setTargetTable(rs.getString("target_table"));
            foreignKey.setTargetKey(rs.getString("target_key"));

            return foreignKey;
        }
    }

    private static class PrimaryKeyMapper implements ParameterizedRowMapper<PrimaryKey> {
        public PrimaryKey mapRow(ResultSet rs, int rowNum) throws SQLException {
            PrimaryKey primaryKey = new PrimaryKey();

            primaryKey.setColumnName(rs.getString("column_name"));

            return primaryKey;
        }
    }
}
