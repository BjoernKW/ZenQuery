package com.zenquery.api;

import com.thoughtworks.xstream.XStream;
import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.Query;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.util.BasicDataSourceFactory;
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
    private PropertiesFactoryBean databaseDriverProperties;

    @Autowired
    private DatabaseConnectionDAO databaseConnectionDAO;

    @Autowired
    private QueryDAO queryDAO;

    @Autowired
    private BasicDataSourceFactory dataSourceFactory;

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = { "application/json; charset=utf-8" })
    public @ResponseBody
    List<Map<String, Object>> currentQuery(
            @PathVariable Integer id
    ) {
        List<Map<String, Object>> rows = getResultRows(id, null);

        return rows;
    }

    @RequestMapping(
            value = "/{id}/{variables}",
            method = RequestMethod.GET,
            produces = { "application/json; charset=utf-8" })
    public @ResponseBody
    List<Map<String, Object>> currentQuery(
            @PathVariable Integer id,
            @PathVariable String variables
    ) {
        List<Map<String, Object>> rows = getResultRows(id, variables);

        return rows;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = { "text/csv; charset=utf-8" })
    public @ResponseBody
    String currentQueryAsCSV(
            @PathVariable Integer id
    ) {
        List<Map<String, Object>> rows = getResultRows(id, null);

        StringBuilder csvBuilder = getCsvBuilder(rows);

        return csvBuilder.toString();
    }

    @RequestMapping(
            value = "/{id}/{variables}",
            method = RequestMethod.GET,
            produces = { "text/csv; charset=utf-8" })
    public @ResponseBody
    String currentQueryAsCSV(
            @PathVariable Integer id,
            @PathVariable String variables
    ) {
        List<Map<String, Object>> rows = getResultRows(id, variables);

        StringBuilder csvBuilder = getCsvBuilder(rows);

        return csvBuilder.toString();
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = { "application/xml; charset=utf-8" })
    public @ResponseBody
    String currentQueryAsXML(
            @PathVariable Integer id
    ) {
        List<Map<String, Object>> rows = getResultRows(id, null);

        XStream stream = getXMLStream();

        return stream.toXML(rows);
    }

    @RequestMapping(
            value = "/{id}/{variables}",
            method = RequestMethod.GET,
            produces = { "application/xml; charset=utf-8" })
    public @ResponseBody
    String currentQueryAsXML(
            @PathVariable Integer id,
            @PathVariable String variables
    ) {
        List<Map<String, Object>> rows = getResultRows(id, variables);

        XStream stream = getXMLStream();

        return stream.toXML(rows);
    }

    private List<Map<String, Object>> getResultRows(Integer id, String variables) {
        Query query = queryDAO.find(id);
        DatabaseConnection databaseConnection = databaseConnectionDAO.find(query.getDatabaseConnectionId());

        Pattern pattern = Pattern.compile("jdbc:(\\w+?):");
        Matcher matcher = pattern.matcher(databaseConnection.getUrl());

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        try {
            String driverClassName = "";
            String validationQuery = "";
            if (matcher.find()) {
                driverClassName = databaseDriverProperties.getObject().getProperty("drivers." + matcher.group(1));
                validationQuery = databaseDriverProperties.getObject().getProperty("validationQueries." + matcher.group(1));
            }

            BasicDataSource dataSource = dataSourceFactory.getBasicDataSource(
                    driverClassName,
                    databaseConnection.getUrl(),
                    databaseConnection.getUsername(),
                    databaseConnection.getPassword(),
                    validationQuery
            );

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            if (variables != null) {
                List<Object> arguments = new ArrayList<Object>();
                String[] extractedVariables = variables.split(",");

                for (String variable : extractedVariables) {
                    try {
                        arguments.add(Long.parseLong(variable));
                    } catch(NumberFormatException noLong) {
                        try {
                            arguments.add(Double.parseDouble(variable));
                        } catch(NumberFormatException noDouble) {
                            arguments.add(variable);
                        }
                    }
                }

                rows = jdbcTemplate.queryForList(query.getContent(), arguments.toArray());
            } else {
                rows = jdbcTemplate.queryForList(query.getContent());
            }

            dataSource.getConnection().close();
            dataSource.close();
        } catch (Exception e) {
            logger.debug(e);
        }

        return rows;
    }

    private StringBuilder getCsvBuilder(List<Map<String, Object>> rows) {
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
        return csvBuilder;
    }

    private XStream getXMLStream() {
        XStream stream = new XStream();
        stream.registerConverter(new MapEntryConverter());
        stream.alias("root", Map.class);
        return stream;
    }
}