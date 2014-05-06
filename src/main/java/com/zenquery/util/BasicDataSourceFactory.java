package com.zenquery.util;

import org.apache.commons.dbcp.BasicDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by willy on 23.04.14.
 */
public class BasicDataSourceFactory {
    private Properties databaseDriverProperties;

    private Map<String, BasicDataSource> dataSources;

    public BasicDataSourceFactory() {
        dataSources = new HashMap<String, BasicDataSource>();
    }

    public void setDatabaseDriverProperties(Properties databaseDriverProperties) {
        this.databaseDriverProperties = databaseDriverProperties;
    }

    public BasicDataSource getBasicDataSource(
            String url,
            String username,
            String password
    ) {
        Pattern pattern = Pattern.compile("jdbc:(\\w+?):");
        Matcher matcher = pattern.matcher(url);

        String driverClassName = "";
        String validationQuery = "";
        if (matcher.find()) {
            driverClassName = databaseDriverProperties.getProperty("drivers." + matcher.group(1));
            validationQuery = databaseDriverProperties.getProperty("validationQueries." + matcher.group(1));
        }

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
