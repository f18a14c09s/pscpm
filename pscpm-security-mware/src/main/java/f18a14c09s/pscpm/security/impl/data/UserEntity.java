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

import f18a14c09s.pscpm.security.data.User;

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

    public UserEntity(User user) {
        setDefaultSecretKey(user.getDefaultSecretKey() == null ? null : new PublicKeyEncryptedSecretKeyEntity(user.getDefaultSecretKey()));
        setUserId(user.getUserId());
        setX509CertificateEncoded(user.getX509CertificateEncoded());
        setCachedDisplayName(
                user.getCachedDisplayName());
        setCachedFriendlyName(
                user.getCachedFriendlyName());
        setCachedPubKeyFprintBase64(
                user.getCachedPubKeyFprintBase64());
        setId(
                user.getId());
        setPublicKeyFingerprintAlgorithm(
                user.getPublicKeyFingerprintAlgorithm());
        setType(
                user.getType());
        setUserId(
                user.getUserId());
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
        retval.setDefaultSecretKey(getDefaultSecretKey() == null ? null : getDefaultSecretKey().toPublicKeyEncryptedSecretKey(retval));
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
