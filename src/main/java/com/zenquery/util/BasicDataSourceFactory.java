package com.zenquery.util;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Created by willy on 23.04.14.
 */
public class BasicDataSourceFactory {
    public BasicDataSource getBasicDataSource(
            String driverClassName,
            String url,
            String username,
            String password,
            String validationQuery
    ) {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setMaxIdle(5);
        dataSource.setValidationQuery(validationQuery);

        return dataSource;
    }
}
