package com.zenquery;

import org.jooq.*;
import org.jooq.impl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.jooq.impl.DSL.*;

@Controller
@RequestMapping("/")
public class StartController {
    @Value("#{databaseProperties['jdbc.driverClassName']}")
    private String driverClassName;

    @Value("#{databaseProperties['jdbc.url']}")
    private String databaseURL;

    @Value("#{databaseProperties['jdbc.username']}")
    private String databaseUsername;

    @Value("#{databaseProperties['jdbc.password']}")
    private String databasePassword;

	@RequestMapping(method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
        Connection connection = null;

        try {
            Class.forName(driverClassName).newInstance();
            connection = DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignore) {
                }
            }
        }

        DSLContext create = DSL.using(connection, SQLDialect.MYSQL);

		model.addAttribute("message", "Hello world!");
		return "index";
	}
}