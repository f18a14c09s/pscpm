package org.francisjohnson.pscpm.security.data;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;

import org.francisjohnson.pscpm.general.data.Identifiable;
import org.francisjohnson.pscpm.security.data.x500.CertificateProposedPurpose;
import org.francisjohnson.pscpm.security.data.x500.X500ASN1GeneralName;
import org.francisjohnson.pscpm.security.services.javacrypto.PublicKeyFingerprinter;
import org.francisjohnson.pscpm.general.services.RdnParser;
import org.francisjohnson.pscpm.security.services.javacrypto.X509Adapter;

public class SecurityPrincipal implements Serializable, Identifiable<Long> {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    private transient Logger logger
            = Logger.getLogger(this.getClass().getPackage().getName());
    private Long id;

    private String cachedFriendlyName;

    private String cachedDisplayName;

    private String cachedPubKeyFprintBase64;

    private String publicKeyFingerprintAlgorithm;

    private byte[] x509CertificateEncoded;

    private String type;

    /**
     * User ID is only mandatory when the system relies solely on external
     * facilities (i.e. the Java EE container) for authentication.
     */
    private String userId;

    public SecurityPrincipal() {
    }

    //    public Principal(X509Certificate cert) throws CertificateEncodingException,
    //                                                  NoSuchAlgorithmException {
    //        if (cert != null) {
    //            setPublicKeyFingerprintAlgorithm(FingerprintPrototype.DEFAULT_FINGERPRINT_ALGORITHM);
    //            if (cert.getIssuerX500Principal() != null) {
    //                setCachedDisplayName(RdnParser.getFirstValue(cert.getSubjectX500Principal().getName(),
    //                                                             "cn"));
    //            }
    //            setCachedFriendlyName(proposeFriendlyName(cert));
    //            setCachedPubKeyFprintBase64(computePublicKeyFingerprintBase64(cert.getPublicKey()));
    //            setX509CertificateEncoded(cert.getEncoded());
    //        }
    //    }
    public SecurityPrincipal(String userId,
            X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException {
        setUserId(userId);
        setX509Certificate(cert);
    }

    private String computePublicKeyFingerprintBase64(PublicKey pubKey) throws NoSuchAlgorithmException {
        if (pubKey == null || pubKey.getEncoded() == null
                || pubKey.getEncoded().length < 1) {
            return null;
        } else {
            MessageDigest digest
                    = MessageDigest.getInstance(getPublicKeyFingerprintAlgorithm());
            return Base64.getEncoder().encodeToString(digest.digest(pubKey.getEncoded()));
        }
    }

    private String proposeFriendlyName(X509Certificate cert) {
        CertificateProposedPurpose purpose = CertificateProposedPurpose.infer(cert);
        String cn = RdnParser.getFirstValue(cert.getSubjectX500Principal().getName(X500Principal.RFC2253),
                "cn");
        String org = RdnParser.getFirstValue(cert.getSubjectX500Principal().getName(X500Principal.RFC2253),
                "o");
        return cn + "'s " + org + " " + purpose.getLabel() + " Certificate";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCachedFriendlyName(String cachedFriendlyName) {
        this.cachedFriendlyName = cachedFriendlyName;
    }

    public String getCachedFriendlyName() {
        return cachedFriendlyName;
    }

    public void setCachedDisplayName(String cachedDisplayName) {
        this.cachedDisplayName = cachedDisplayName;
    }

    public String getCachedDisplayName() {
        return cachedDisplayName;
    }

    public void setX509CertificateEncoded(byte[] x509CertificateEncoded) {
        this.x509CertificateEncoded = x509CertificateEncoded;
    }

    public byte[] getX509CertificateEncoded() {
        return x509CertificateEncoded;
    }

    public void setX509Certificate(X509Certificate cert) throws NoSuchAlgorithmException,
            CertificateEncodingException {
        setPublicKeyFingerprintAlgorithm(PublicKeyFingerprinter.DEFAULT_FINGERPRINT_ALGORITHM);
        setCachedDisplayName(cert == null
                || cert.getIssuerX500Principal() == null ? null : RdnParser.getFirstValue(cert.getSubjectX500Principal().getName(),
                "cn"));
        setCachedFriendlyName(proposeFriendlyName(cert));
        setCachedPubKeyFprintBase64(cert == null ? null
                : computePublicKeyFingerprintBase64(cert.getPublicKey()));
        setX509CertificateEncoded(cert == null ? null : cert.getEncoded());
    }

    public X509Certificate getX509Certificate() throws CertificateException {
        return X509Adapter.getX509Certificate(getX509CertificateEncoded());
    }

    public byte[] getCachedPublicKeyFingerprint() {
        if (getCachedPubKeyFprintBase64() == null
                || getCachedPubKeyFprintBase64().isEmpty()) {
            return null;
        } else {
            return Base64.getDecoder().decode(getCachedPubKeyFprintBase64());
        }
    }

    private void setLogger(Logger logger) {
        this.logger = logger;
    }

    private Logger getLogger() {
        return logger;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setCachedPubKeyFprintBase64(String cachedPubKeyFprintBase64) {
        this.cachedPubKeyFprintBase64 = cachedPubKeyFprintBase64;
    }

    public String getCachedPubKeyFprintBase64() {
        return cachedPubKeyFprintBase64;
    }

    public void setPublicKeyFingerprintAlgorithm(String publicKeyFingerprintAlgorithm) {
        this.publicKeyFingerprintAlgorithm = publicKeyFingerprintAlgorithm;
    }

    public String getPublicKeyFingerprintAlgorithm() {
        return publicKeyFingerprintAlgorithm;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRfc822SubjectAlternativeName() throws CertificateException {
        List<String> rfc822Names = new ArrayList<String>();
        for (List<?> subAltName
                : (Collection<List<?>>) getX509Certificate().getSubjectAlternativeNames()) {
            X500ASN1GeneralName generalName = X500ASN1GeneralName.find(((Number) subAltName.get(0)).intValue());
            if (generalName == X500ASN1GeneralName.RFC822_NAME) {
                String r8n = (String) subAltName.get(1);
                if (r8n != null && !r8n.isEmpty()) {
                    rfc822Names.add(r8n);
                }
            }
        }
        if (rfc822Names.size() > 1) {
            System.out.println("Multiple subject alternative RFC 822 names found: "
                    + rfc822Names + ".");
        }
        return rfc822Names.size() != 1 ? null : rfc822Names.get(0);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getUserId();
    }
}
