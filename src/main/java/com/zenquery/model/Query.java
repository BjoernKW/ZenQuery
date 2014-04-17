package com.zenquery.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by willy on 13.04.14.
 */
@XmlRootElement
public class Query implements Serializable {
    Integer id;
    String key;
    String content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDatabaseConnectionId() {
        return databaseConnectionId;
    }

    public void setDatabaseConnectionId(Integer databaseConnectionId) {
        this.databaseConnectionId = databaseConnectionId;
    }
}
