package com.zenquery.model.dao;

import com.zenquery.model.Query;

import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public interface QueryDAO {
    public Query find(Integer id);

    public Query findByKey(String key);

    public List<Query> findByDatabaseConnectionId(Integer id);

    public List<Query> findAll();

    public Number insert(Query query);

    public void update(Integer id, Query query);

    public void delete(Integer id);
}
