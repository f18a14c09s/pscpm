package org.francisjohnson.pscpm.security.data;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;


public class OfflineUser extends User {
    private static final String DEFAULT_USER_ID = "Offline User";

    public OfflineUser() {
        super();
        setUserId(DEFAULT_USER_ID);
    }

    public OfflineUser(X509Certificate cert) throws NoSuchAlgorithmException,
                                                    CertificateEncodingException {
        super();
        setUserId(DEFAULT_USER_ID);
        setX509Certificate(cert);
    }
}
