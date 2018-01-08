package org.francisjohnson.pscpm.secrets.data;

import java.io.Serializable;

public class Application implements Serializable {
    private String description;
    private String url;

    public Application() {
        super();
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
}
