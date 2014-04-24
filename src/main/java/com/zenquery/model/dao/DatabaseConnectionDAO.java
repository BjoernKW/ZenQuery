package com.zenquery.model.dao;

import com.zenquery.model.DatabaseConnection;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public interface DatabaseConnectionDAO {
    @Cacheable("sql.databaseConnections")
    public DatabaseConnection find(Integer id);

    @Cacheable("sql.databaseConnections.lists")
    public List<DatabaseConnection> findAll();

    @CacheEvict(
            value = "sql.databaseConnections.lists",
            allEntries = true
    )
    public Number insert(DatabaseConnection databaseConnection);

    @CacheEvict(
            value = {
                    "sql.databaseConnections",
                    "sql.databaseConnections.lists"
            },
            allEntries = true)
    public void update(Integer id, DatabaseConnection databaseConnection);

    @CacheEvict(
            value = {
                    "sql.databaseConnections",
                    "sql.databaseConnections.lists"
            },
            allEntries = true)
    public void delete(Integer id);
}
