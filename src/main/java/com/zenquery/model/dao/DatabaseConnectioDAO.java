package com.zenquery.model.dao;

import com.zenquery.model.DatabaseConnection;

/**
 * Created by willy on 13.04.14.
 */
public interface DatabaseConnectioDAO {
    public DatabaseConnection find(Integer id);
}
