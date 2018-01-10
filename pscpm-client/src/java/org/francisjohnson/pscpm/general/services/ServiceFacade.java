package org.francisjohnson.pscpm.general.services;

import java.io.IOException;

import java.net.URISyntaxException;

import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.secrets.services.ISecretsService;
import org.francisjohnson.pscpm.secrets.services.SecretsServiceJaxRsClientImpl;
import org.francisjohnson.pscpm.security.services.ISecurityService;
import org.francisjohnson.pscpm.security.services.SecurityServiceJaxRsClientImpl;

public class ServiceFacade<Iface extends Object> {

    public static final String DEFAULT_SERVER_URL = "https://localhost:8181/pscpm-services-web";

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
