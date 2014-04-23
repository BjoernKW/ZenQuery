package com.zenquery.util;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Created by willy on 23.04.14.
 */
public interface DataSourceFactory {
    public BasicDataSource getBasicDataSource(
            String driverClassName,
            String url,
            String username,
            String password
    );
}
