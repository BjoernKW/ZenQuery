package com.zenquery.api;

import com.thoughtworks.xstream.XStream;
import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.Query;
import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.model.dao.QueryVersionDAO;
import com.zenquery.util.MapEntryConverter;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/api/v1/resultSetForQuery")
public class ResultSetController {
    private static final Logger logger = Logger.getLogger(ResultSetController.class);

    @Autowired
    private PropertiesFactoryBean driverClassNameProperties;

    @Autowired
    private DatabaseConnectionDAO databaseConnectionDAO;

    @Autowired
    private QueryDAO queryDAO;

    @Autowired
    private QueryVersionDAO queryVersionDAO;

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = { "application/json" })
    public @ResponseBody
    List<Map<String, Object>> currentQuery(
            @PathVariable Integer id
    ) {
        Query query = queryDAO.find(id);
        DatabaseConnection databaseConnection = databaseConnectionDAO.find(query.getDatabaseConnectionId());

        Pattern pattern = Pattern.compile("jdbc:(\\w+?):");
        Matcher matcher = pattern.matcher(databaseConnection.getUrl());

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        try {
            String driverClassName = "";
            if (matcher.find()) {
                driverClassName = driverClassNameProperties.getObject().getProperty(matcher.group(1));
            }

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUsername(databaseConnection.getUsername());
            dataSource.setPassword(databaseConnection.getPassword());
            dataSource.setUrl(databaseConnection.getUrl());
            dataSource.setMaxIdle(5);
            dataSource.setValidationQuery("SELECT 1");

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            rows = jdbcTemplate.queryForList(query.getContent());
        } catch (Exception e) {
            logger.debug(e);
        }

        return rows;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = { "text/csv" })
    public @ResponseBody
    String currentQueryAsCSV(
            @PathVariable Integer id
    ) {
        Query query = queryDAO.find(id);
        DatabaseConnection databaseConnection = databaseConnectionDAO.find(query.getDatabaseConnectionId());

        Pattern pattern = Pattern.compile("jdbc:(\\w+?):");
        Matcher matcher = pattern.matcher(databaseConnection.getUrl());

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        try {
            String driverClassName = "";
            if (matcher.find()) {
                driverClassName = driverClassNameProperties.getObject().getProperty(matcher.group(1));
            }

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUsername(databaseConnection.getUsername());
            dataSource.setPassword(databaseConnection.getPassword());
            dataSource.setUrl(databaseConnection.getUrl());
            dataSource.setMaxIdle(5);
            dataSource.setValidationQuery("SELECT 1");

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            rows = jdbcTemplate.queryForList(query.getContent());
        } catch (Exception e) {
            logger.debug(e);
        }

        StringBuilder csvBuilder = new StringBuilder();
        Boolean first = true;

        for (Map<String, Object> row : rows) {
            if (first) {
                for (String key : row.keySet()) {
                    csvBuilder.append(key + ";");
                }
                csvBuilder.append("\r\n");
                first = false;
            }

            for (String key : row.keySet()) {
                csvBuilder.append(row.get(key) + ";");
            }
            csvBuilder.append("\r\n");
        }

        return csvBuilder.toString();
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = { "application/xml" })
    public @ResponseBody
    String currentQueryAsXML(
            @PathVariable Integer id
    ) {
        Query query = queryDAO.find(id);
        DatabaseConnection databaseConnection = databaseConnectionDAO.find(query.getDatabaseConnectionId());

        Pattern pattern = Pattern.compile("jdbc:(\\w+?):");
        Matcher matcher = pattern.matcher(databaseConnection.getUrl());

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        try {
            String driverClassName = "";
            if (matcher.find()) {
                driverClassName = driverClassNameProperties.getObject().getProperty(matcher.group(1));
            }

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUsername(databaseConnection.getUsername());
            dataSource.setPassword(databaseConnection.getPassword());
            dataSource.setUrl(databaseConnection.getUrl());
            dataSource.setMaxIdle(5);
            dataSource.setValidationQuery("SELECT 1");

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            rows = jdbcTemplate.queryForList(query.getContent());
        } catch (Exception e) {
            logger.debug(e);
        }

        XStream stream = new XStream();
        stream.registerConverter(new MapEntryConverter());
        stream.alias("root", Map.class);

        return stream.toXML(rows);
    }

	@RequestMapping(
            value = "/{id}/{version}",
            method = RequestMethod.GET,
            produces = { "application/xml", "application/json", "text/csv" })
	public @ResponseBody
    List<Map<String, Object>> queryByVersion(
            @PathVariable Integer id,
            @PathVariable Integer version
    ) {
        Query query = queryDAO.find(id);
        DatabaseConnection databaseConnection = databaseConnectionDAO.find(query.getDatabaseConnectionId());
        QueryVersion queryVersion = queryVersionDAO.findByQueryIdAndVersion(query.getId(), version);

        Pattern pattern = Pattern.compile("jdbc:(\\w+?):");
        Matcher matcher = pattern.matcher(databaseConnection.getUrl());

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        try {
            String driverClassName = "";
            if (matcher.find()) {
                driverClassName = driverClassNameProperties.getObject().getProperty(matcher.group(1));
            }

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(driverClassName);
            dataSource.setUsername(databaseConnection.getUsername());
            dataSource.setPassword(databaseConnection.getPassword());
            dataSource.setUrl(databaseConnection.getUrl());
            dataSource.setMaxIdle(5);
            dataSource.setValidationQuery("SELECT 1");

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            rows = jdbcTemplate.queryForList(queryVersion.getContent());
        } catch (Exception e) {
            logger.debug(e);
        }

        return rows;
	}
}