package com.zenquery.api;

import com.zenquery.model.Query;
import com.zenquery.model.dao.QueryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/v1/queries")
public class QueryController {
    @Autowired
    private QueryDAO queryDAO;

    @Cacheable("api.queries")
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
    Query find(@PathVariable Integer id) {
		return queryDAO.find(id);
	}

    @Cacheable("api.queries")
    @RequestMapping(value = "/findByDatabaseConnectionId/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Query> findByDatabaseConnectionId(@PathVariable Integer id) {
        return queryDAO.findByDatabaseConnectionId(id);
    }

    @RequestMapping(value = "{key}/{databaseConnectionId}", method = RequestMethod.POST)
    public @ResponseBody
    Query create(
            @PathVariable String key,
            @PathVariable Integer databaseConnectionId
    ) {
        Query query = new Query();
        query.setKey(key);

        Number id = queryDAO.insert(query);

        return queryDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(value = "{id}/{key}", method = RequestMethod.PUT)
    public @ResponseBody String update(
            @PathVariable Integer id,
            @PathVariable String key,
            @PathVariable String url,
            @PathVariable String username,
            @PathVariable String password
    ) {
        Query query = new Query();
        query.setKey(key);

        queryDAO.update(id, query);

        return "OK";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Integer id) {
        queryDAO.delete(id);

        return "OK";
    }
}