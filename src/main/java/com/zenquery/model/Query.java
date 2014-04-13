package com.zenquery.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public class Query implements Serializable {
    Integer id;
    String key;
    List<QueryVersion> queryVersions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<QueryVersion> getQueryVersions() {
        return queryVersions;
    }

    public void setQueryVersions(List<QueryVersion> queryVersions) {
        this.queryVersions = queryVersions;
    }
}
