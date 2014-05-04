package com.zenquery.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by willy on 23.04.14.
 */
public class BasicDataSourceFactory {
    private Map<String, BasicDataSource> dataSources;

    public BasicDataSourceFactory() {
        dataSources = new HashMap<String, BasicDataSource>();
    }

    public BasicDataSource getBasicDataSource(
            String driverClassName,
            String url,
            String username,
            String password,
            String validationQuery
    ) {

        BasicDataSource dataSource = dataSources.get(url);

        if (dataSource == null) {
            dataSource = new BasicDataSource();

            dataSource.setDriverClassName(driverClassName);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setUrl(url);
            dataSource.setMaxIdle(5);
            dataSource.setValidationQuery(validationQuery);

            dataSources.put(url, dataSource);
        }

        return dataSource;
    }
}
