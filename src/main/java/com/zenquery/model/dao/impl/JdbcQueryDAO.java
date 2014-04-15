package com.zenquery.model.dao.impl;

import com.zenquery.model.Query;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.security.MessageDigest;
import java.sql.*;
import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public class JdbcQueryDAO implements QueryDAO {
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Cacheable("sql.queries")
    public Query find(Integer id) {
        String sql = "SELECT * FROM queries WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        Query query =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryMapper()).get(0);

        return query;
    }

    @Cacheable("sql.queries")
    public List<Query> findByDatabaseConnectionId(Integer id) {
        String sql = "SELECT * FROM queries WHERE database_connection_id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Query> queries =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryMapper());

        return queries;
    }

    public List<Query> findAll() {
        String sql = "SELECT * FROM queries";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Query> queries =
                jdbcTemplate.query(sql, new QueryMapper());

        return queries;
    }

    public Number insert(final Query query) {
        final String sql = "INSERT INTO queries (key) VALUES (?)";

        query.setKey(StringUtil.hashWithSha256(query.getKey()));

        jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql.toString(), new String[] { "id" });

                preparedStatement.setString(1, query.getKey());

                return preparedStatement;
            }
        };
        jdbcTemplate.update(
                preparedStatementCreator,
                keyHolder
        );

        return keyHolder.getKey();
    }

    public void update(Integer id, Query query) {
        String sql = "UPDATE queries SET key = ? WHERE id = ?";

        query.setKey(StringUtil.hashWithSha256(query.getKey()));

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(
                sql,
                new Object[] {
                        query.getKey(),
                        id
                }
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM queries WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[] { id });
    }

    private static class QueryMapper implements ParameterizedRowMapper<Query> {
        public Query mapRow(ResultSet rs, int rowNum) throws SQLException {
            Query query = new Query();

            query.setId(rs.getInt("id"));
            query.setKey(rs.getString("key"));
            query.setDatabaseConnectionId(rs.getInt("database_connection_id"));

            return query;
        }
    }
}
