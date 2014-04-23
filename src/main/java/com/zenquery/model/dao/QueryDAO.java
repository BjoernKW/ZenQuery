package com.zenquery.model.dao;

import com.zenquery.model.Query;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public interface QueryDAO {
    @Cacheable("sql.queries")
    public Query find(Integer id);

    @Cacheable("sql.queries")
    public Query findByKey(String key);

    @Cacheable("sql.queries")
    public List<Query> findByDatabaseConnectionId(Integer id);

    public List<Query> findAll();

    public Number insert(Query query);

    public void update(Integer id, Query query);

    public void delete(Integer id);

    public void deleteByDatabaseConnectionId(Integer id);
}
