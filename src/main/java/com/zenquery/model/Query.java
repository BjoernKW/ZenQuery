package com.zenquery.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by willy on 13.04.14.
 */
public class Query implements Serializable {
    Integer id;
    String key;
    Integer databaseConnectionId;

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

    public Integer getDatabaseConnectionId() {
        return databaseConnectionId;
    }

    public void setDatabaseConnectionId(Integer databaseConnectionId) {
        this.databaseConnectionId = databaseConnectionId;
    }
}
