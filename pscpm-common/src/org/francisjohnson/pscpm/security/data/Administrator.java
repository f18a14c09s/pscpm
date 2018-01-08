package org.francisjohnson.pscpm.security.data;

import java.io.Serializable;



public class Administrator extends User implements Serializable {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public Administrator() {
    }
}
