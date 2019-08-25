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
package f18a14c09s.pscpm.security.impl.data;

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

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.security.auth.x500.X500Principal;

import f18a14c09s.pscpm.general.data.Identifiable;
import f18a14c09s.pscpm.security.data.x500.CertificateProposedPurpose;
import f18a14c09s.pscpm.security.data.x500.X500ASN1GeneralName;
import f18a14c09s.pscpm.security.services.javacrypto.PublicKeyFingerprinter;
import f18a14c09s.pscpm.general.services.RdnParser;
import f18a14c09s.pscpm.security.data.SecurityPrincipal;

@Entity
@NamedQueries({
    @NamedQuery(name = "SecurityPrincipalEntity.findAll",
            query = "select o from SecurityPrincipalEntity o")
    ,@NamedQuery(name = "SecurityPrincipalEntity.findByUserId", query = "select o from SecurityPrincipalEntity o where o.userId = :userid")
    ,@NamedQuery(name = "SecurityPrincipalEntity.findByPubKeyFprint", query = "select o from SecurityPrincipalEntity o where o.cachedPubKeyFprintBase64 = :fingerprint")})
@Inheritance
@SequenceGenerator(name = "SECURITY_PRINCIPAL_ID_S",
        sequenceName = "SECURITY_PRINCIPAL_ID_S",
        allocationSize = 1, initialValue = 1)
@Table(name = "SECURITY_PRINCIPALS")
@DiscriminatorColumn(name = "TYPE",
        length=1000,discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("UNAUTHORIZED")
public class SecurityPrincipalEntity implements Serializable, Identifiable<Long> {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    @Transient
    private transient Logger logger
            = Logger.getLogger(this.getClass().getPackage().getName());
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "SECURITY_PRINCIPAL_ID_S")
    @Column(name = "ID", nullable=false,precision=38,scale=0)
    private Long id;

    @Column(name = "CACHED_FRIENDLY_NAME", length=1000)
    private String cachedFriendlyName;

    @Column(name = "CACHED_DISPLAY_NAME",
            length=1000, nullable=false)
    private String cachedDisplayName;

    @Column(name = "CACHED_PUB_KEY_FPRINT_BASE64",
            length=1000, nullable=false)
    private String cachedPubKeyFprintBase64;

    @Column(name = "PUB_KEY_FINGERPRINT_ALGORITHM",
            length=1000, nullable=false)
    private String publicKeyFingerprintAlgorithm;

    @Column(name = "X_509_CERTIFICATE_ENCODED",
            nullable=false)
    @Lob
    private byte[] x509CertificateEncoded;

    @Column(name = "TYPE", insertable = false, updatable = false)
    private String type;

    /**
     * User ID is only mandatory when the system relies solely on external
     * facilities (i.e. the Java EE container) for authentication.
     */
    @Column(name = "USER_ID", length=1000, nullable=false)
    private String userId;

    public SecurityPrincipalEntity() {
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
    public SecurityPrincipalEntity(String userId,
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

    protected void setId(Long id) {
        this.id = id;
    }

    protected void setCachedFriendlyName(String cachedFriendlyName) {
        this.cachedFriendlyName = cachedFriendlyName;
    }

    public String getCachedFriendlyName() {
        return cachedFriendlyName;
    }

    protected void setCachedDisplayName(String cachedDisplayName) {
        this.cachedDisplayName = cachedDisplayName;
    }

    public String getCachedDisplayName() {
        return cachedDisplayName;
    }

    protected void setX509CertificateEncoded(byte[] x509CertificateEncoded) {
        this.x509CertificateEncoded = x509CertificateEncoded;
    }

    byte[] getX509CertificateEncoded() {
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
        if (getX509CertificateEncoded() == null
                || getX509CertificateEncoded().length < 1) {
            return null;
        } else {
            ByteArrayInputStream baos
                    = new ByteArrayInputStream(getX509CertificateEncoded());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate retval
                    = (X509Certificate) cf.generateCertificate(baos);
            try {
                baos.close();
            } catch (Exception e) {
                getLogger().log(Level.FINE,
                        "ByteArrayInputStream failed to close.  This likely did not impede processing.",
                        e);
            }
            return retval;
        }
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

    protected void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    protected void setCachedPubKeyFprintBase64(String cachedPubKeyFprintBase64) {
        this.cachedPubKeyFprintBase64 = cachedPubKeyFprintBase64;
    }

    public String getCachedPubKeyFprintBase64() {
        return cachedPubKeyFprintBase64;
    }

    protected void setPublicKeyFingerprintAlgorithm(String publicKeyFingerprintAlgorithm) {
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

    public SecurityPrincipal toSecurityPrincipal() {
        SecurityPrincipal retval = new SecurityPrincipal();
        retval.setUserId(getUserId());
        retval.setX509CertificateEncoded(getX509CertificateEncoded());
        retval.setCachedDisplayName(
                getCachedDisplayName());
        retval.setCachedFriendlyName(
                getCachedFriendlyName());
        retval.setCachedPubKeyFprintBase64(
                getCachedPubKeyFprintBase64());
        retval.setId(
                getId());
        retval.setPublicKeyFingerprintAlgorithm(
                getPublicKeyFingerprintAlgorithm());
        retval.setType(
                getType());
        retval.setUserId(
                getUserId());
        return retval;
    }
}
