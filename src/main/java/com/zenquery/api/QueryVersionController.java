package com.zenquery.api;

import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.QueryVersionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/queryVersions")
public class QueryVersionController {
    @Autowired
    private QueryVersionDAO queryVersionDAO;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/xml", "application/json" })
	public @ResponseBody
    QueryVersion find(@PathVariable Integer id) {
		return queryVersionDAO.find(id);
	}

    @RequestMapping(value = "/findByQueryId/{id}", method = RequestMethod.GET, produces = { "application/xml", "application/json" })
    public @ResponseBody
    List<QueryVersion> findByQueryId(@PathVariable Integer id) {
        return queryVersionDAO.findByQueryId(id);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = { "application/xml", "application/json" },
            produces = { "application/xml", "application/json" })
    public @ResponseBody
    QueryVersion create(
            @RequestBody QueryVersion queryVersion
    ) {
        Number id = queryVersionDAO.insert(queryVersion);

        return queryVersionDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(
            value = "/{id}",
            consumes = { "application/xml", "application/json" },
            method = RequestMethod.PUT)
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