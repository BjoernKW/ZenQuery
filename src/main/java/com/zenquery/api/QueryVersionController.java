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

    @RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
    public @ResponseBody
    List<QueryVersion> findAll() {
        return queryVersionDAO.findAll();
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
	public @ResponseBody
    QueryVersion find(@PathVariable Integer id) {
		return queryVersionDAO.find(id);
	}

    @RequestMapping(value = "/findCurrentByQueryId/{id}", method = RequestMethod.GET, produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
    public @ResponseBody
    QueryVersion findCurrentByQueryId(@PathVariable Integer id) {
        return queryVersionDAO.findCurrentByQueryId(id);
    }

    @RequestMapping(value = "/findByQueryId/{id}", method = RequestMethod.GET, produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
    public @ResponseBody
    List<QueryVersion> findByQueryId(@PathVariable Integer id) {
        return queryVersionDAO.findByQueryId(id);
    }

    @RequestMapping(value = "/findPreviousVersionsByQueryId/{id}", method = RequestMethod.GET, produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
    public @ResponseBody
    List<QueryVersion> findPreviousVersionsByQueryId(@PathVariable Integer id) {
        return queryVersionDAO.findPreviousVersionsByQueryId(id);
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = { "application/xml; charset=utf-8", "application/json; charset=utf-8" },
            produces = { "application/xml; charset=utf-8", "application/json; charset=utf-8" })
    public @ResponseBody
    QueryVersion create(
            @RequestBody QueryVersion queryVersion
    ) {
        Number id = queryVersionDAO.insert(queryVersion);

        return queryVersionDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(
            value = "/{id}",
            consumes = { "application/xml; charset=utf-8", "application/json; charset=utf-8" },
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