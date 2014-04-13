package com.zenquery.model.dao.impl;

import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.QueryVersionDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public QueryVersion find(Integer id) {
        String sql = "SELECT * FROM query_versions WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);

        QueryVersion queryVersion =
                jdbcTemplate.query(sql, new Object[] { id }, new QueryVersionMapper()).get(0);

        return queryVersion;
    }

    public List<QueryVersion> findByQueryId(Integer id) {
        String sql = "SELECT * FROM query_versions WHERE query_id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.setFetchSize(1);

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

    public void insert(QueryVersion queryVersion) {
        String sql = "INSERT INTO query_versions (content, version, is_current_version) VALUES (?, ?, ?)";

        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(sql, new Object[] {
                queryVersion.getContent(),
                queryVersion.getVersion(),
                queryVersion.getIsCurrentVersion()
        });
    }

    public void update(Integer id, QueryVersion queryVersion) {
        String sql = "UPDATE query_versions SET content = ?, version = ?, is_current_version = ? WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(sql, new Object[]{
                queryVersion.getContent(),
                queryVersion.getVersion(),
                queryVersion.getIsCurrentVersion(),
                id
        });
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM query_versions WHERE id = ?";

        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(sql);
    }

    private static class QueryVersionMapper implements ParameterizedRowMapper<QueryVersion> {
        public QueryVersion mapRow(ResultSet rs, int rowNum) throws SQLException {
            QueryVersion queryVersion = new QueryVersion();

            queryVersion.setId(rs.getInt("id"));
            queryVersion.setContent(rs.getString("name"));
            queryVersion.setVersion(rs.getInt("version"));
            queryVersion.setIsCurrentVersion(rs.getBoolean("is_current_version"));

            return queryVersion;
        }
    }
}
