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
package f18a14c09s.pscpm.secrets.data;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


public class Server implements Serializable {
    /**
     * This value must not change, or else the data in the database will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    /**
     * This is transient because it's the ID of the object that contains it,
     * and can change over time while this resides in the DB.
     */
    private transient Long id;
    private String name;
    private String environment;
    private String osVersion;
    private String ipAddress;
    private String description;
    private List<URL> urls = new ArrayList<URL>();
    private List<UserAccount> credentials = new ArrayList<UserAccount>();

    public Server() {
        super();
    }

    public Server(String name, String environment, String osVersion,
                  String ipAddress, String description) {
        super();
        setName(name);
        setEnvironment(environment);
        setOsVersion(osVersion);
        setIpAddress(ipAddress);
        setDescription(description);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public void clearUrls() {
        getUrls().clear();
    }

    public void addUrl(URL url) {
        getUrls().add(url);
    }

    public List<URL> getUrls() {
        return urls;
    }

    private List<UserAccount> filterCredentials(UserAccount.Type type) {
        List<UserAccount> retval = new ArrayList<UserAccount>();
        if (getCredentials() != null) {
            for (UserAccount cred : getCredentials()) {
                if (cred.getType() == type) {
                    retval.add(cred);
                }
            }
        }
        return retval;
    }

    public List<UserAccount> getAppCredentials() {
        return filterCredentials(UserAccount.Type.APPLICATION);
    }

    public List<UserAccount> getAdminCredentials() {
        return filterCredentials(UserAccount.Type.ADMINISTRATIVE);
    }

    public void setCredentials(List<UserAccount> credentials) {
        if (credentials != null) {
            getCredentials().addAll(credentials);
        }
        this.credentials = credentials;
    }

    public List<UserAccount> getCredentials() {
        return credentials;
    }

    public void addCredential(UserAccount cred) {
        getCredentials().add(cred);
    }

    public void removeCredential(UserAccount cred) {
        getCredentials().remove(cred);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
