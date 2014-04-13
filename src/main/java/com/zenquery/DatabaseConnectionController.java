package com.zenquery;

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
@RequestMapping("/databaseConnections")
public class DatabaseConnectionController {
    @Autowired
    private DatabaseConnectionDAO databaseConnectionDAO;

    @Cacheable("databaseConnections")
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
    DatabaseConnection find(@PathVariable Integer id) {
		return databaseConnectionDAO.find(id);
	}
}