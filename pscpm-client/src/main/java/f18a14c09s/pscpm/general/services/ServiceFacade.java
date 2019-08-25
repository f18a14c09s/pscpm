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
package f18a14c09s.pscpm.general.services;

import java.io.IOException;

import java.net.URISyntaxException;

import f18a14c09s.pscpm.security.data.javacrypto.UserCredential;
import f18a14c09s.pscpm.secrets.services.ISecretsService;
import f18a14c09s.pscpm.secrets.services.SecretsServiceJaxRsClientImpl;
import f18a14c09s.pscpm.security.services.ISecurityService;
import f18a14c09s.pscpm.security.services.SecurityServiceJaxRsClientImpl;

public class ServiceFacade<Iface extends Object> {

    public static final String DEFAULT_SERVER_URL = "https://pscpm.francisjohnson.org/pscpm-services";

    private Class<Iface> iface;
    private String serverUrl;
    private UserCredential cred;

    private ServiceFacade(Class<Iface> iface, String serverUrl,
            UserCredential cred) throws NullPointerException,
            IOException,
            URISyntaxException {
        setIface(iface);
        setServerUrl(serverUrl);
        setCred(cred);
    }

    public static <Iface> ServiceFacade<Iface> getInstance(Class<Iface> iface,
            String serverUrl,
            UserCredential cred) throws IOException,
            URISyntaxException {
        return new ServiceFacade<Iface>(iface, serverUrl, cred);
    }

    public Iface newFacade() {
        // TODO: Pass in the UserCredential or private key alias (or other identifier) and ensure that the JAX-RS client uses the specified private key.
        if (getIface().equals(ISecurityService.class)) {
            return (Iface) new SecurityServiceJaxRsClientImpl(getCred().getKeyStore(), getServerUrl());
        } else if (getIface().equals(ISecretsService.class)) {
            return (Iface) new SecretsServiceJaxRsClientImpl(getCred().getKeyStore(), getServerUrl());
        }
        return null;
    }

    private void setIface(Class<Iface> iface) {
        this.iface = iface;
    }

    private Class<Iface> getIface() {
        return iface;
    }

    private void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    private String getServerUrl() {
        return serverUrl;
    }

    private void setCred(UserCredential cred) {
        this.cred = cred;
    }

    private UserCredential getCred() {
        return cred;
    }
}
