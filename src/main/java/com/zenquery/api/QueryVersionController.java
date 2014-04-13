package com.zenquery.api;

import com.zenquery.model.QueryVersion;
import com.zenquery.model.dao.QueryVersionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/v1/queryVersions")
public class QueryVersionController {
    @Autowired
    private QueryVersionDAO queryVersionDAO;

    @Cacheable("api.queryVersions")
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "{content}/{version}/{is_current_version}/{query_id}", method = RequestMethod.POST)
    public @ResponseBody
    QueryVersion create(
            @PathVariable String content,
            @PathVariable Integer version,
            @PathVariable Boolean isCurrentVersion,
            @PathVariable Integer queryId
    ) {
        QueryVersion queryVersion = new QueryVersion();
        queryVersion.setContent(content);
        queryVersion.setVersion(version);
        queryVersion.setIsCurrentVersion(isCurrentVersion);
        queryVersion.setQueryId(queryId);

        Number id = queryVersionDAO.insert(queryVersion);

        return queryVersionDAO.find(new Long(id.longValue()).intValue());
    }

    @RequestMapping(value = "{id}/{content}/{version}/{is_current_version}/{query_id}", method = RequestMethod.PUT)
    public @ResponseBody String update(
            @PathVariable Integer id,
            @PathVariable String content,
            @PathVariable Integer version,
            @PathVariable Boolean isCurrentVersion,
            @PathVariable Integer queryId
    ) {
        QueryVersion queryVersion = new QueryVersion();
        queryVersion.setContent(content);
        queryVersion.setVersion(version);
        queryVersion.setIsCurrentVersion(isCurrentVersion);
        queryVersion.setQueryId(queryId);

        queryVersionDAO.update(id, queryVersion);

        return "OK";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public @ResponseBody String delete(@PathVariable Integer id) {
        queryVersionDAO.delete(id);

        return "OK";
    }
}