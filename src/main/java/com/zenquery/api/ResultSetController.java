package com.zenquery.api;

import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.Query;
import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import com.zenquery.model.dao.QueryDAO;
import com.zenquery.model.dao.QueryVersionDAO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping(value = "/{key}/{version}", method = RequestMethod.GET, produces = { "application/xml", "application/json" })
	public @ResponseBody
    List<Map<String, Object>> query(
            @PathVariable String key,
            @PathVariable Integer version
    ) {
        Query query = queryDAO.findByKey(key);
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