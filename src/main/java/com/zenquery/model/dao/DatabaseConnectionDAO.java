package com.zenquery.model.dao;

import com.zenquery.model.DatabaseConnection;

import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public interface DatabaseConnectionDAO {
    public DatabaseConnection find(Integer id);

    public List<DatabaseConnection> findAll();

    public Number insert(DatabaseConnection databaseConnection);

    public void update(Integer id, DatabaseConnection databaseConnection);

    public void delete(Integer id);
}
