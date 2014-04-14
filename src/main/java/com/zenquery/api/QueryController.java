package com.zenquery.api;

import com.zenquery.model.Query;
import com.zenquery.model.dao.QueryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/queries")
public class QueryController {
    @Autowired
    private QueryDAO queryDAO;

    @Cacheable("api.queries")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
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

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    Query create(
            @RequestBody Query query
    ) {
        Number id = queryDAO.insert(query);

        return queryDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody String update(
            @PathVariable Integer id,
            @RequestBody Query query
    ) {
        queryDAO.update(id, query);

        return "OK";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Integer id) {
        queryDAO.delete(id);

        return "OK";
    }
}