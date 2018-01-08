package org.francisjohnson.pscpm.general.services.weblogic;

import java.io.IOException;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.naming.Context;
import javax.naming.NamingException;

import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.security.services.ISecurityService;
import org.francisjohnson.pscpm.security.services.SecuritySvcRemoteFacadeBean;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;

import weblogic.jndi.Environment;
import weblogic.jndi.WLInitialContextFactory;


public class WLSRMISunMSCAPIFacade {
    public static final String DEFAULT_RMI_PROVIDER_URL =
        "t3s://localhost:7002";
    public static final long DEFAULT_REQUEST_TIMEOUT_SECONDS = 30;

    public static void main(String[] args) {
        try {
            final Context context =
                getInitialContext(DEFAULT_RMI_PROVIDER_URL, IdentityKeyStoreAdapter.getAdvancedSignatureCredential("Francis Johnson"));
            ISecurityService iSecurityService =
                //                (ISecurityService)context.lookup("SecurityPrototyping-RemoteSecurityServicePrototype-RemoteSecurityServiceSession#org.francisjohnson.pscpm.security.services.ISecurityService")
                RemoteEJBFacadeFinder.getFacade(context,
                                                SecuritySvcRemoteFacadeBean.class);
            User user = iSecurityService.getCurrentUser();
            System.out.println("Current user: " +
                               (user == null ? null : user.getCachedFriendlyName()) +
                               ".");
            for (PublicKeyEncryptedSecretKey key :
                 iSecurityService.findUserSecretKeys()) {
                System.out.println("Encrypted Symmetric Key: " +
                                   key.getEncryptedSymmetricKeyBase64());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Context getInitialContext(String serverUrl,
                                            UserCredential cred) throws NamingException,
                                                                        KeyStoreException,
                                                                        IOException,
                                                                        NoSuchAlgorithmException,
                                                                        CertificateException,
                                                                        UnrecoverableKeyException,
                                                                        ClassNotFoundException,
                                                                        InstantiationException,
                                                                        IllegalAccessException {
        Security.addProvider(new WLSCompatibleSunMSCAPIWrapper());
        //        UserCredential cred =
        //            PublicKeyIdentityStoreAdapter.getAdvancedSignatureCredential();
        Environment envE = new Environment();
        // TODO: Remove this in "Production."
        envE.setRequestTimeout(DEFAULT_REQUEST_TIMEOUT_SECONDS * 1000L);
        envE.setInitialContextFactory(WLInitialContextFactory.class.getName());
        envE.setProviderURL(serverUrl);
        envE.loadLocalIdentity(cred.getKeyStore().getCertificateChain(cred.getAlias()),
                               cred.getKey());
        //        Security.insertProviderAt(new SunMSCAPIWrapper(),1);
        //        Security.setProperty("keystore.type", SunMSCAPIWrapper.WindowsMyKeyStoreWrapperSpi.MY_KEY_STORE_TYPE);
        return envE.getInitialContext();
    }
}
