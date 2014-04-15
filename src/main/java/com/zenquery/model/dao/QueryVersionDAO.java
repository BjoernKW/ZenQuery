package com.zenquery.model.dao;

import com.zenquery.model.QueryVersion;

import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public interface QueryVersionDAO {
    public QueryVersion find(Integer id);

    public QueryVersion findByQueryIdAndVersion(Integer id, Integer version);

    public List<QueryVersion> findByQueryId(Integer id);

    public List<QueryVersion> findAll();

    public Number insert(QueryVersion QueryVersion);

    public void update(Integer id, QueryVersion QueryVersion);

    public void delete(Integer id);
}
