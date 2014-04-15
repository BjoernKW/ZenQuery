package com.zenquery.model.dao.impl;

import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.QueryVersionDAO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public class JdbcQueryVersionDAO implements QueryVersionDAO {
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Cacheable("sql.queryVersions")
    public QueryVersion find(Integer id) {
        String sql = "SELECT * FROM query_versions WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        QueryVersion queryVersion =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryVersionMapper()).get(0);

        return queryVersion;
    }

    @Cacheable("sql.queryVersions")
    public List<QueryVersion> findByQueryId(Integer id) {
        String sql = "SELECT * FROM query_versions WHERE query_id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<QueryVersion> queryVersions =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryVersionMapper());

        return queryVersions;
    }

    public List<QueryVersion> findAll() {
        String sql = "SELECT * FROM query_versions";

        jdbcTemplate = new JdbcTemplate(dataSource);
        List<QueryVersion> queryVersions =
                jdbcTemplate.query(sql, new QueryVersionMapper());

        return queryVersions;
    }

    public Number insert(final QueryVersion queryVersion) {
        final String sql = "INSERT INTO query_versions (content, version, is_current_version) VALUES (?, ?, ?)";

        jdbcTemplate = new JdbcTemplate(dataSource);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[] { "id" });

                preparedStatement.setString(1, queryVersion.getContent());
                preparedStatement.setInt(2, queryVersion.getVersion());
                preparedStatement.setBoolean(3, queryVersion.getIsCurrentVersion());

                return preparedStatement;
            }
        };
        jdbcTemplate.update(
                preparedStatementCreator,
                keyHolder
        );

        return keyHolder.getKey();
    }

    public void update(Integer id, QueryVersion queryVersion) {
        String sql = "UPDATE query_versions SET content = ?, version = ?, is_current_version = ? WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(
                sql,
                new Object[] {
                        queryVersion.getContent(),
                        queryVersion.getVersion(),
                        queryVersion.getIsCurrentVersion(),
                        id
                }
        );
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM query_versions WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[] { id });
    }

    private static class QueryVersionMapper implements ParameterizedRowMapper<QueryVersion> {
        public QueryVersion mapRow(ResultSet rs, int rowNum) throws SQLException {
            QueryVersion queryVersion = new QueryVersion();

            queryVersion.setId(rs.getInt("id"));
            queryVersion.setContent(rs.getString("content"));
            queryVersion.setVersion(rs.getInt("version"));
            queryVersion.setIsCurrentVersion(rs.getBoolean("is_current_version"));
            queryVersion.setQueryId(rs.getInt("query_id"));

            return queryVersion;
        }
    }
}
