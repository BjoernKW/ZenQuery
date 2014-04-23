package com.zenquery.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by willy on 23.04.14.
 */
public interface DatabaseConnectionStore {
    @Cacheable("databaseConnections")
    public BasicDataSource getBasicDataSource(
            String driverClassName,
            String url,
            String username,
            String password
    );
}
