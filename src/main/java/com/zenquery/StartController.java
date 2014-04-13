package com.zenquery;

import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Tr;
import com.hp.gagawa.java.elements.Th;
import com.hp.gagawa.java.elements.Td;
import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.Query;
import com.zenquery.model.QueryVersion;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/")
public class StartController {
    private static final Logger logger = Logger.getLogger(StartController.class);

    @Autowired
    private PropertiesFactoryBean driverClassNameProperties;

    @RequestMapping(method = RequestMethod.GET)
    public String welcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");

        return "index";
    }

	@RequestMapping(value = "/test")
	public String test(ModelMap model) {
        List<Map<String, Object>> rows = getDatabaseConnections(model);

        Table table = new Table();
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
                td.appendText(row.get(key).toString());
                tableRow.appendChild(td);
            }

            if (firstRow) {
                table.appendChild(tableHeader);
                firstRow = false;
            }
            table.appendChild(tableRow);
        }

        model.addAttribute("result", table.write());

		return "test";
	}

    @RequestMapping(value = "/test", produces = { "application/json" })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    List<Map<String, Object>> testAPI(ModelMap model) {
        List<Map<String, Object>> rows = getDatabaseConnections(model);

        return rows;
    }

    private List<Map<String, Object>> getDatabaseConnections(ModelMap model) {
        Map<String, Integer> variables = new HashMap<String, Integer>();
        variables.put("databaseConnectionId", 1);

        RestTemplate restTemplate = new RestTemplate();
        DatabaseConnection databaseConnection = restTemplate.getForObject(
                "http://localhost:8080/api/v1/databaseConnections/{databaseConnectionId}",
                DatabaseConnection.class, variables
        );

        Query[] queries = restTemplate.getForObject(
                "http://localhost:8080/api/v1/queries/findByDatabaseConnectionId/{databaseConnectionId}",
                Query[].class, variables
        );

        variables.remove("databaseConnectionId");
        variables.put("queryId", queries[0].getId());
        QueryVersion[] queryVersions = restTemplate.getForObject(
                "http://localhost:8080/api/v1/queryVersions/findByQueryId/{queryId}",
                QueryVersion[].class, variables
        );
        String queryContent = "";
        for (QueryVersion queryVersion : queryVersions) {
            if (queryVersion.getIsCurrentVersion()) {
                queryContent = queryVersion.getContent();
            }
        }

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
            rows = jdbcTemplate.queryForList(queryContent);
        } catch (Exception e) {
            logger.debug(e);
        }

        return rows;
    }
}