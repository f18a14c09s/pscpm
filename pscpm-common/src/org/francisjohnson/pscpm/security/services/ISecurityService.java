package org.francisjohnson.pscpm.security.services;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.SecurityPrincipal;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.general.services.IService;

public interface ISecurityService extends IService {

    User authenticate(X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException;

    SecurityPrincipal findPrincipal(X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException;

    PublicKeyEncryptedSecretKey newSecretKey(String keyAlias)
            //                      , X509Certificate cert
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, CertificateException;

    void share(PublicKeyEncryptedSecretKey key, User with);

    <L extends List<PublicKeyEncryptedSecretKey> & Serializable> L findUserSecretKeys();

    User getCurrentUser();

    void setUserCertificate(X509Certificate cert) throws NoSuchAlgorithmException,
            CertificateEncodingException;

    User mergeUser(User user);
}
