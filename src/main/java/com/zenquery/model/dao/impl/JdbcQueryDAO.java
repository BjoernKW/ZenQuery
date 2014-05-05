package com.zenquery.model.dao.impl;

import com.zenquery.model.Query;
import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.model.dao.QueryVersionDAO;
import com.zenquery.util.StringUtil;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
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
public class JdbcQueryDAO implements QueryDAO {
    private URI dbUrl;

    private Properties databaseDriverProperties;

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private QueryVersionDAO queryVersionDAO;

    public void setDbUrl(URI dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void setDatabaseDriverProperties(Properties databaseDriverProperties) {
        this.databaseDriverProperties = databaseDriverProperties;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setQueryVersionDAO(QueryVersionDAO queryVersionDAO) {
        this.queryVersionDAO = queryVersionDAO;
    }

    public Query find(Integer id) {
        String sql = databaseDriverProperties.getProperty(dbUrl.getScheme() + ".queries.find");

        jdbcTemplate = new JdbcTemplate(dataSource);
        Query query =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryMapper()).get(0);

        return query;
    }

    public Query findByKey(String key) {
        String sql = databaseDriverProperties.getProperty(dbUrl.getScheme() + ".queries.findByKey");

        jdbcTemplate = new JdbcTemplate(dataSource);
        Query query =
                jdbcTemplate.query(sql, new Object[] { key }, new QueryMapper()).get(0);

        return query;
    }

    public List<Query> findByDatabaseConnectionId(Integer id) {
        String sql = databaseDriverProperties.getProperty(dbUrl.getScheme() + ".queries.findByDatabaseConnectionId");

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Query> queries =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryMapper());

        return queries;
    }

    public List<Query> findAll() {
        String sql = databaseDriverProperties.getProperty(dbUrl.getScheme() + ".queries.findAll");

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Query> queries =
                jdbcTemplate.query(sql, new QueryMapper());

        return queries;
    }

    public Number insert(final Query query) {
        final String sql = "INSERT INTO queries (key, database_connection_id) VALUES (?, ?)";

        query.setKey(StringUtil.hashWithSha256(new Double(Math.random()).toString() + System.currentTimeMillis()));

        jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[] { "id" });

                preparedStatement.setString(1, query.getKey());
                preparedStatement.setInt(2, query.getDatabaseConnectionId());

                return preparedStatement;
            }
        };
        jdbcTemplate.update(
                preparedStatementCreator,
                keyHolder
        );

        QueryVersion queryVersion = new QueryVersion();
        queryVersion.setQueryId(keyHolder.getKey().intValue());
        queryVersion.setContent(query.getContent());
        queryVersion.setIsCurrentVersion(true);
        queryVersion.setVersion(1);
        queryVersionDAO.insert(queryVersion);

        return keyHolder.getKey();
    }

    public void update(Integer id, Query query) {
        QueryVersion previousQueryVersion = queryVersionDAO.findCurrentByQueryId(id);

        if (!query.getContent().equals(previousQueryVersion.getContent())) {
            previousQueryVersion.setIsCurrentVersion(false);
            queryVersionDAO.update(previousQueryVersion.getId(), previousQueryVersion);

            QueryVersion queryVersion = new QueryVersion();
            queryVersion.setQueryId(id);
            queryVersion.setContent(query.getContent());
            queryVersion.setIsCurrentVersion(true);
            queryVersion.setVersion(previousQueryVersion.getVersion() + 1);

            queryVersionDAO.insert(queryVersion);
        }
    }

    public void delete(Integer id) {
        queryVersionDAO.deleteByQueryId(id);

        String sql = "DELETE FROM queries WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[] { id });
    }

    public void deleteByDatabaseConnectionId(Integer id) {
        List<Query> queries = findByDatabaseConnectionId(id);
        for (Query query : queries) {
            queryVersionDAO.deleteByQueryId(query.getId());
        }

        String sql = "DELETE FROM queries WHERE database_connection_id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[] { id });
    }

    private static class QueryMapper implements ParameterizedRowMapper<Query> {
        public Query mapRow(ResultSet rs, int rowNum) throws SQLException {
            Query query = new Query();

            query.setId(rs.getInt("id"));
            query.setKey(rs.getString("key"));
            query.setContent(rs.getString("content"));
            query.setDatabaseConnectionId(rs.getInt("database_connection_id"));

            return query;
        }
    }
}
