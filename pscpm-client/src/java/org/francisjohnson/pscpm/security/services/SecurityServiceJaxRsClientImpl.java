/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.francisjohnson.pscpm.security.services;

import java.io.IOException;
import java.io.Serializable;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.francisjohnson.pscpm.general.services.IOUtil;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.SecurityPrincipal;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author fjohnson
 */
public class SecurityServiceJaxRsClientImpl implements ISecurityService {

    private final Logger _log = Logger.getLogger(getClass().getName());

    @Override
    public User authenticate(X509Certificate cert) throws CertificateEncodingException {
//        System.out.println(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "Certificate " + (cert == null ? "is null" : "is non-null")));
        if (cert != null) {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            formData.add("user_cert", Base64.getEncoder().encodeToString(cert.getEncoded()));
            Client client = ClientBuilder.newClient();
            byte[] user = client.target("http://localhost:8080/pscpm-services-web/security/authenticate")
                    .request()
                    .post(Entity.form(formData), byte[].class);
//            System.out.println(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "User bytes: " + (user == null ? null : "byte[" + user.length) + "]" + "."));
            if (user != null && user.length >= 1) {
                try {
                    return IOUtil.deserialize(user);
                } catch (IOException | ClassNotFoundException e) {
                    _log.log(Level.SEVERE, "Unable to deserialize User object.", e);
                }
            }
        }
        return null;
    }

    @Override
    public SecurityPrincipal findPrincipal(X509Certificate cert) {
        return null;
    }

    @Override
    public PublicKeyEncryptedSecretKey newSecretKey(String keyAlias) {
        return null;
    }

    @Override
    public void share(PublicKeyEncryptedSecretKey key, User with) {
    }

    @Override
    public <L extends List<PublicKeyEncryptedSecretKey> & Serializable> L findUserSecretKeys() {
        return null;
    }

    @Override
    public User getCurrentUser() {
//        System.out.println(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "Certificate " + (cert == null ? "is null" : "is non-null")));
            Client client = ClientBuilder.newClient();
            byte[] user = client.target("http://localhost:8080/pscpm-services-web/security/authenticate")
                    .request()
                    .get(byte[].class);
//            System.out.println(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "User bytes: " + (user == null ? null : "byte[" + user.length) + "]" + "."));
            if (user != null && user.length >= 1) {
                try {
                    return IOUtil.deserialize(user);
                } catch (IOException | ClassNotFoundException e) {
                    _log.log(Level.SEVERE, "Unable to deserialize User object.", e);
                }
            }
        return null;
    }

    @Override
    public void setUserCertificate(X509Certificate cert) {
    }

    @Override
    public User mergeUser(User user) {
        return null;
    }

//set MYCP=C:\Program Files\NetBeans 8.2\enterprise\modules\ext\jaxrs-2.0\javax.ws.rs-api-2.0.jar
//set MYCP=%MYCP%;C:\Users\fjohnson\Documents\NetBeansProjects\PSCPM\pscpm-client\dist\pscpm-client.jar
//set MYCP=%MYCP%;c:\Users\fjohnson\Documents\NetBeansProjects\PSCPM\pscpm-common\dist\pscpm-common.jar
//set MYCP=%MYCP%;C:\Program Files\NetBeans 8.2\enterprise\modules\ext\jersey2\jersey-client.jar
//set MYCP=%MYCP%;C:\Program Files\NetBeans 8.2\enterprise\modules\ext\jersey2\jersey-common.jar;
//set MYCP=%MYCP%;C:\Program Files\glassfish-5.0\glassfish\modules\hk2-utils.jar
//set MYCP=%MYCP%;C:\Program Files\glassfish-5.0\glassfish\modules\hk2-api.jar
//set MYCP=%MYCP%;C:\Program Files\glassfish-5.0\glassfish\modules\hk2-core.jar
//set MYCP=%MYCP%;C:\Program Files\NetBeans 8.2\ide\modules\com-google-guava.jar
//set MYCP=%MYCP%;C:\Program Files\NetBeans 8.2\ide\modules\com-googlecode-javaewah-JavaEWAH.jar
//set MYCP=%MYCP%;C:\Program Files\glassfish-5.0\glassfish\modules\jersey-hk2.jar
//set MYCP=%MYCP%;C:\Program Files\glassfish-5.0\glassfish\modules\hk2-config.jar
//set MYCP=%MYCP%;C:\Program Files\glassfish-5.0\glassfish\modules\hk2.jar
//
//java -cp "%MYCP%" org.francisjohnson.pscpm.security.services.SecurityServiceJaxRsClientImpl
    public static void main(String... args) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2017, 1, 1);
            UserCredential cred = IdentityKeyStoreAdapter.getBasicEncryptionCredential("Francis Johnson", cal.getTime());
            X509Certificate cert = cred.getCert();
            SecurityServiceJaxRsClientImpl svc = new SecurityServiceJaxRsClientImpl();
            User currentUser = svc.authenticate(cert);
            System.out.println("User: " + currentUser + ".");
        } catch (Exception e) {
            System.exit(1);
        }
    }
}
