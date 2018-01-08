package org.francisjohnson.pscpm.secrets.data;

import java.io.Serializable;


import org.francisjohnson.pscpm.security.data.UserSecretKey;


public class CertificateSecret extends Secret<Certificate> implements Serializable {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public CertificateSecret() {
    }

    public CertificateSecret(byte[] encryptedData, UserSecretKey secretKey,
                             byte[] initVector, String cipherAlgorithm) {
        super(encryptedData, secretKey, initVector, cipherAlgorithm);
    }

    public Certificate getCert() {
        return super.getData();
    }
}
