package org.francisjohnson.pscpm.security.data;

import java.io.Serializable;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class User extends SecurityPrincipal implements Serializable {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    private PublicKeyEncryptedSecretKey defaultSecretKey;

    public User() {
    }

    //    public User(X509Certificate cert) throws CertificateEncodingException,
    //                                             NoSuchAlgorithmException {
    //        super(cert);
    //    }

    public User(String userId,
                X509Certificate cert) throws CertificateEncodingException,
                                             NoSuchAlgorithmException {
        super(userId, cert);
    }

    public void setDefaultSecretKey(PublicKeyEncryptedSecretKey defaultSecretKey) {
        this.defaultSecretKey = defaultSecretKey;
    }

    public PublicKeyEncryptedSecretKey getDefaultSecretKey() {
        return defaultSecretKey;
    }
}
