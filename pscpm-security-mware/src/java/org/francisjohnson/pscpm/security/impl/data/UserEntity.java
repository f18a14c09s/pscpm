package org.francisjohnson.pscpm.security.impl.data;

import java.io.Serializable;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.francisjohnson.pscpm.security.data.User;

@Entity
@NamedQueries({
    @NamedQuery(name = "UserEntity.findAll",
            query = "select o from UserEntity o")
    ,@NamedQuery(name = "UserEntity.findByUserId", query = "select o from UserEntity o where o.userId = :userid")})
@Inheritance
@DiscriminatorValue("USER")
public class UserEntity extends SecurityPrincipalEntity implements Serializable {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    @ManyToOne
    @JoinColumn(name = "DEFAULT_SECRET_KEY_ID", referencedColumnName = "ID"
//            ,precision=38,scale=0
    )
    private PublicKeyEncryptedSecretKeyEntity defaultSecretKey;

    public UserEntity() {
    }

    //    public User(X509Certificate cert) throws CertificateEncodingException,
    //                                             NoSuchAlgorithmException {
    //        super(cert);
    //    }
    public UserEntity(String userId,
            X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException {
        super(userId, cert);
    }

    public void setDefaultSecretKey(PublicKeyEncryptedSecretKeyEntity defaultSecretKey) {
        this.defaultSecretKey = defaultSecretKey;
    }

    public PublicKeyEncryptedSecretKeyEntity getDefaultSecretKey() {
        return defaultSecretKey;
    }

    public User toUser() {
        User retval = new User();
        retval.setDefaultSecretKey(getDefaultSecretKey() == null ? null : getDefaultSecretKey().toPublicKeyEncryptedSecretKey());
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
