package com.zenquery.api;

import com.hp.gagawa.java.elements.Strong;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import com.hp.gagawa.java.elements.Th;
import com.hp.gagawa.java.elements.Div;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/resultSetForQuery")
public class ResultSetController {
    private static final Logger logger = Logger.getLogger(ResultSetController.class);

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

    @RequestMapping(
            value = "/{mode}/{complete}/{id}",
            method = RequestMethod.GET,
            produces = { "text/html; charset=utf-8" })
    public @ResponseBody
    String currentQueryAsHHTML(
            @PathVariable Integer id,
            @PathVariable String mode,
            @PathVariable Boolean complete
    ) {
        List<Map<String, Object>> rows = getResultRows(id, null);

        String html = getHTML(mode, complete, rows);

        return html;
    }

    @RequestMapping(
            value = "/{mode}/{complete}/{id}/{variables}",
            method = RequestMethod.GET,
            produces = { "text/html; charset=utf-8" })
    public @ResponseBody
    String currentQueryAsHTML(
            @PathVariable Integer id,
            @PathVariable String variables,
            @PathVariable String mode,
            @PathVariable Boolean complete
    ) {
        List<Map<String, Object>> rows = getResultRows(id, variables);

        String html = getHTML(mode, complete, rows);

        return html;
    }

    private List<Map<String, Object>> getResultRows(Integer id, String variables) {
        Query query = queryDAO.find(id);
        DatabaseConnection databaseConnection = databaseConnectionDAO.find(query.getDatabaseConnectionId());

        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        try {
            BasicDataSource dataSource = dataSourceFactory.getBasicDataSource(
                    databaseConnection.getUrl(),
                    databaseConnection.getUsername(),
                    databaseConnection.getPassword()
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

    private String getHTML(String mode, Boolean complete, List<Map<String, Object>> rows) {
        String html;

        if (mode.equals("horizontal")) {
            if (complete) {
                html = getHorizontalResultListHTML(rows);
            } else {
                html = getHorizontalTableHTML(rows);
            }
        } else {
            if (complete) {
                html = getVerticalResultListHTML(rows);
            } else {
                html = getVerticalListHTML(rows);
            }
        }
        return html;
    }

    private String getHorizontalTableHTML(List<Map<String, Object>> rows) {
        Table table = new Table();
        table.setCSSClass("table table-striped table-bordered table-hover");

        Tr tableHeader = new Tr();
        Boolean firstRow = true;

        for (Map<String, Object> row : rows) {
            Tr tableRow = new Tr();

            for (String key : row.keySet()) {
                if (firstRow) {
                    Th th = new Th();
                    th.appendText(key);
                    tableHeader.appendChild(th);
                }

                Td td = new Td();
                tableRow.appendChild(td);

                Object value = row.get(key);
                if (value != null) {
                    td.appendText(value.toString());
                }
            }

            if (firstRow) {
                table.appendChild(tableHeader);
                firstRow = false;
            }
            table.appendChild(tableRow);
        }

        return table.write();
    }

    private String getVerticalListHTML(List<Map<String, Object>> rows) {
        Div resultSetList = new Div();
        resultSetList.setCSSClass("row");

        for (Map<String, Object> row : rows) {
            Div entry = new Div();
            entry.setCSSClass("col-lg-12");
            resultSetList.appendChild(entry);

            Table entryTable = new Table();
            entryTable.setCSSClass("table table-striped table-bordered table-hover");
            entry.appendChild(entryTable);

            for (String key : row.keySet()) {
                Tr attributeRow = new Tr();

                Td tdKey = new Td();
                Strong strong = new Strong();
                tdKey.appendChild(strong);
                strong.appendText(key);
                attributeRow.appendChild(tdKey);

                Td tdValue = new Td();
                attributeRow.appendChild(tdValue);

                Object value = row.get(key);
                if (value != null) {
                    tdValue.appendText(value.toString());
                }

                entryTable.appendChild(attributeRow);
            }
        }

        return resultSetList.write();
    }

    private String getHorizontalResultListHTML(List<Map<String, Object>> rows) {
        String html = "<!doctype html><!--[if lt IE 7]>      <html class=\"no-js lt-ie9 lt-ie8 lt-ie7\"> <![endif]--><!--[if IE 7]>         <html class=\"no-js lt-ie9 lt-ie8\"> <![endif]--><!--[if IE 8]>         <html class=\"no-js lt-ie9\"> <![endif]--><!--[if gt IE 8]><!--> <html class=\"no-js\"> <!--<![endif]--><head><meta charset=\"utf-8\"><title>禅宗 - ZenQuery</title><meta name=\"description\" content=\"\"><meta name=\"viewport\" content=\"width=device-width\"><link rel=\"stylesheet\" href=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css\"><link rel=\"stylesheet\" href=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css\"><script src=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js\"></script></head><body><div class=\"container\">";

        html += getHorizontalTableHTML(rows);

        html += "</div></body></html>";

        return  html;
    }

    private String getVerticalResultListHTML(List<Map<String, Object>> rows) {
        String html = "<!doctype html><!--[if lt IE 7]>      <html class=\"no-js lt-ie9 lt-ie8 lt-ie7\"> <![endif]--><!--[if IE 7]>         <html class=\"no-js lt-ie9 lt-ie8\"> <![endif]--><!--[if IE 8]>         <html class=\"no-js lt-ie9\"> <![endif]--><!--[if gt IE 8]><!--> <html class=\"no-js\"> <!--<![endif]--><head><meta charset=\"utf-8\"><title>禅宗 - ZenQuery</title><meta name=\"description\" content=\"\"><meta name=\"viewport\" content=\"width=device-width\"><link rel=\"stylesheet\" href=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css\"><link rel=\"stylesheet\" href=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css\"><script src=\"//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js\"></script></head><body><div class=\"container\">";

        html += getVerticalListHTML(rows);

        html += "</div></body></html>";

        return  html;
    }
}
