package com.zenquery.model.dao.impl;

import com.zenquery.model.Query;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.model.dao.QueryVersionDAO;
import com.zenquery.util.StringUtil;
import org.springframework.cache.annotation.Cacheable;
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
import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public class JdbcQueryDAO implements QueryDAO {
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private QueryVersionDAO queryVersionDAO;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setQueryVersionDAO(QueryVersionDAO queryVersionDAO) {
        this.queryVersionDAO = queryVersionDAO;
    }

    @Cacheable("sql.queries")
    public Query find(Integer id) {
        String sql = "SELECT q.*, qv.content FROM queries AS q LEFT OUTER JOIN query_versions AS qv ON q.id = qv.query_id AND qv.is_current_version = TRUE WHERE q.id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        Query query =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryMapper()).get(0);

        return query;
    }

    @Cacheable("sql.queries")
    public Query findByKey(String key) {
        String sql = "SELECT q.*, qv.content FROM queries AS q LEFT OUTER JOIN query_versions AS qv ON q.id = qv.query_id AND qv.is_current_version = TRUE WHERE q.key = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        Query query =
                jdbcTemplate.query(sql, new Object[] { key }, new QueryMapper()).get(0);

        return query;
    }

    @Cacheable("sql.queries")
    public List<Query> findByDatabaseConnectionId(Integer id) {
        String sql = "SELECT q.*, qv.content FROM queries AS q LEFT OUTER JOIN query_versions AS qv ON q.id = qv.query_id AND qv.is_current_version = TRUE WHERE q.database_connection_id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<Query> queries =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryMapper());

        return queries;
    }

    public List<Query> findAll() {
        String sql = "SELECT q.*, qv.content FROM queries AS q LEFT OUTER JOIN query_versions AS qv ON q.id = qv.query_id AND qv.is_current_version = TRUE";


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
        queryVersionDAO.deleteByQueryId(id);

        String sql = "DELETE FROM queries WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[] { id });
    }

    public void deleteByDatabaseConnectionId(Integer id) {
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
