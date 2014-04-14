package com.zenquery.api;

import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.QueryVersionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/queryVersions")
public class QueryVersionController {
    @Autowired
    private QueryVersionDAO queryVersionDAO;

    @Cacheable("api.queryVersions")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
    QueryVersion find(@PathVariable Integer id) {
		return queryVersionDAO.find(id);
	}

    @Cacheable("api.queryVersions")
    @RequestMapping(value = "/findByQueryId/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<QueryVersion> findByQueryId(@PathVariable Integer id) {
        return queryVersionDAO.findByQueryId(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public @ResponseBody
    QueryVersion create(
            @RequestBody QueryVersion queryVersion
    ) {
        Number id = queryVersionDAO.insert(queryVersion);

        return queryVersionDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody String update(
            @PathVariable Integer id,
            @RequestBody QueryVersion queryVersion
    ) {
        queryVersionDAO.update(id, queryVersion);

        return "OK";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Integer id) {
        queryVersionDAO.delete(id);

        return "OK";
    }
}