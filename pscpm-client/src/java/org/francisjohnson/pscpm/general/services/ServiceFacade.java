package org.francisjohnson.pscpm.general.services;

import java.io.IOException;

import java.net.URISyntaxException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.naming.Context;
import javax.naming.NamingException;

import org.francisjohnson.pscpm.secrets.services.SecretsSvcRemoteFacadeBean;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.security.services.SecuritySvcRemoteFacadeBean;
import org.francisjohnson.pscpm.general.services.weblogic.RemoteEJBFacadeFinder;
import org.francisjohnson.pscpm.general.services.weblogic.WLSRMISunMSCAPIFacade;


public class ServiceFacade<Iface extends Object> {
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

    public Iface newFacade() throws InstantiationException,
                                    IllegalAccessException, NamingException,
                                    KeyStoreException, IOException,
                                    NoSuchAlgorithmException,
                                    CertificateException,
                                    UnrecoverableKeyException,
                                    ClassNotFoundException {
        try {
            final Context context = WLSRMISunMSCAPIFacade.getInitialContext(getServerUrl(),
                                                       getCred());
            for (Class<?> clazz :
                 new Class<?>[] { SecuritySvcRemoteFacadeBean.class,
                                  SecretsSvcRemoteFacadeBean.class }) {
                for (Class<?> iface : clazz.getInterfaces()) {
                    if (iface.equals(getIface())) {
                        return RemoteEJBFacadeFinder.getFacade(context,
                                                                    (Class<Iface>)clazz);
                    }
                }
            }
        } finally {
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
