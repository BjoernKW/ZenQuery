package com.zenquery.model.dao;

import com.zenquery.model.QueryVersion;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public interface QueryVersionDAO {
    @Cacheable("sql.queryVersions")
    public QueryVersion find(Integer id);

    @Cacheable("sql.queryVersions")
    public QueryVersion findByQueryIdAndVersion(Integer id, Integer version);

    public QueryVersion findCurrentByQueryId(Integer id);

    public List<QueryVersion> findByQueryId(Integer id);

    public List<QueryVersion> findPreviousVersionsByQueryId(Integer id);

    public List<QueryVersion> findAll();

    public Number insert(QueryVersion QueryVersion);

    public void update(Integer id, QueryVersion QueryVersion);

    public void delete(Integer id);

    public void deleteByQueryId(Integer id);
}
