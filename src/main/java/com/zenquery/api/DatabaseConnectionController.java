package com.zenquery.api;

import com.zenquery.model.DatabaseConnection;
import com.zenquery.model.dao.DatabaseConnectionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/databaseConnections")
public class DatabaseConnectionController {
    @Autowired
    private DatabaseConnectionDAO databaseConnectionDAO;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
    public @ResponseBody
    List<DatabaseConnection> findAll() {
        return databaseConnectionDAO.findAll();
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
	public @ResponseBody
    DatabaseConnection find(@PathVariable Integer id) {
		return databaseConnectionDAO.find(id);
	}

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = { "application/xml; charset=utf-8", "application/json; charset=utf-8" },
            produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
    public @ResponseBody
    DatabaseConnection create(
            @RequestBody DatabaseConnection databaseConnection
    ) {
        Number id = databaseConnectionDAO.insert(databaseConnection);

        return databaseConnectionDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(
            value = "/{id}",
            consumes = { "application/xml; charset=utf-8", "application/json; charset=utf-8" },
            method = RequestMethod.PUT)
    public @ResponseBody String update(
            @PathVariable Integer id,
            @RequestBody DatabaseConnection databaseConnection
    ) {
        databaseConnectionDAO.update(id, databaseConnection);

        return "OK";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Integer id) {
        databaseConnectionDAO.delete(id);

        return "OK";
    }
}