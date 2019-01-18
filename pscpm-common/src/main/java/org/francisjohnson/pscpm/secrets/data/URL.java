/* 
 * Copyright (C) 2018 Francis Johnson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
