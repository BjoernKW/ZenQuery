package com.zenquery.api;

import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/databaseConnections")
public class DatabaseConnectionController {
    @Autowired
    private DatabaseConnectionDAO databaseConnectionDAO;

    @Cacheable("api.databaseConnections")
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
    DatabaseConnection find(@PathVariable Integer id) {
		return databaseConnectionDAO.find(id);
	}

    @RequestMapping(value = "{name}/{url}/{username}/{password}", method = RequestMethod.POST)
    public @ResponseBody
    DatabaseConnection create(
            @PathVariable String name,
            @PathVariable String url,
            @PathVariable String username,
            @PathVariable String password
    ) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.setName(name);
        databaseConnection.setUrl(url);
        databaseConnection.setUsername(username);
        databaseConnection.setPassword(password);

        Number id = databaseConnectionDAO.insert(databaseConnection);

        return databaseConnectionDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(value = "{id}/{name}/{url}/{username}/{password}", method = RequestMethod.PUT)
    public @ResponseBody String update(
            @PathVariable Integer id,
            @PathVariable String name,
            @PathVariable String url,
            @PathVariable String username,
            @PathVariable String password
    ) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.setName(name);
        databaseConnection.setUrl(url);
        databaseConnection.setUsername(username);
        databaseConnection.setPassword(password);

        databaseConnectionDAO.update(id, databaseConnection);

        return "OK";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Integer id) {
        databaseConnectionDAO.delete(id);

        return "OK";
    }
}