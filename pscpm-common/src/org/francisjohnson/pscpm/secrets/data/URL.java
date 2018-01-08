package org.francisjohnson.pscpm.secrets.data;

import java.io.Serializable;


public class URL implements Serializable {
    /**
     * This value must not change, or else the data in the database will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public void setType(URL.Type type) {
        this.type = type;
    }

    public URL.Type getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static enum Type {
        UNKNOWN,
        ADMINISTRATIVE;
    }
    private Type type = URL.Type.UNKNOWN;
    private String description;
    private String url;

    public URL() {
        super();
    }
}
