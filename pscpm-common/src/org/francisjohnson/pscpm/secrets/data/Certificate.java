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

import java.util.Date;


public class Certificate implements Serializable {
    /**
     * This value must not change, or else the data in the database will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public static enum StoreType {
        UNKNOWN,
        JKS,
        WALLET,
        PKCS12,
        PKCS11;
    }
    private String name;
    private String environment;
    private String requestNumber;
    private String certAuthorityId;
    private Date expirationDate;
    private StoreType storeType;
    private String storeName;
    private String location;
    private byte[] storePassword;

    /**
     * Private key password.
     */
    private byte[] aliasPassword;

    public Certificate() {
        super();
    }

    public Certificate(String name, String environment, String requestNumber,
                       String certAuthorityId, Date expirationDate,
                       StoreType storeType, String storeName, String location,
                       byte[] storePassword, byte[] aliasPassword) {
        super();
        setName(name);
        setEnvironment(environment);
        setRequestNumber(requestNumber);
        setCertAuthorityId(certAuthorityId);
        setExpirationDate(expirationDate);
        setStoreType(storeType);
        setStoreName(storeName);
        setLocation(location);
        setStorePassword(storePassword);
        setAliasPassword(aliasPassword);
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

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setCertAuthorityId(String certAuthorityId) {
        this.certAuthorityId = certAuthorityId;
    }

    public String getCertAuthorityId() {
        return certAuthorityId;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setStoreType(Certificate.StoreType storeType) {
        this.storeType = storeType;
    }

    public Certificate.StoreType getStoreType() {
        return storeType;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setStorePassword(byte[] storePassword) {
        this.storePassword = storePassword;
    }

    public byte[] getStorePassword() {
        return storePassword;
    }

    public void setAliasPassword(byte[] aliasPassword) {
        this.aliasPassword = aliasPassword;
    }

    public byte[] getAliasPassword() {
        return aliasPassword;
    }
}
