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
package f18a14c09s.pscpm.secrets.impl.data;

import java.io.Serializable;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import f18a14c09s.pscpm.secrets.data.Secret;
import f18a14c09s.pscpm.security.impl.data.UserSecretKeyEntity;
import f18a14c09s.pscpm.general.data.Identifiable;

@Entity
@NamedQueries({
    @NamedQuery(name = "SecretEntity.findAll",
            query = "select o from SecretEntity o")})
@Inheritance
@SequenceGenerator(name = "SECRET_ID_S", sequenceName = "SECRET_ID_S",
        allocationSize = 1, initialValue = 1)
@Table(name = "SECRETS")
@DiscriminatorColumn(name = "TYPE", length = 100)
@DiscriminatorValue("UNKNOWN")
public abstract class SecretEntity<EntityClass> implements Serializable,
        Identifiable<Long> {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "SECRET_ID_S")
    private Long id;
    @Version
    private Long version;
    @ManyToOne
    @JoinColumn(name = "SECRET_KEY_ID", referencedColumnName = "ID",
            nullable = false
    //    ,precision=38,scale=0
    )
    private UserSecretKeyEntity secretKey;

    /**
     * Optional depending on the cryptographic algorithm used. The base-64
     * encoded, initialization vector used to encrypt the data stored in
     * ENCRYPTED_DATA.
     */
    @Column(name = "CIPHER_INITIALIZATION_VECTOR",
            length = 4000)
    private String cipherInitVectorBase64;

    @Column(name = "ENCRYPTED_DATA", nullable = false)
    private byte[] encryptedData;

    /**
     * Identifies the cryptographic algorithm for which the symmetric key,
     * stored in ENCRYPTED_SYMMETRIC_KEY column, is used. Example: AES.
     */
    @Column(name = "CIPHER_TRANSFORMATION",
            length = 100, nullable = false)
    private String cipherTransformation;
    @Transient
    private transient EntityClass data;

    public SecretEntity() {
    }

    //    public Secret(byte[] encryptedData, UserSecretKey secretKey,
    //                  byte[] initVector) {
    //        setEncryptedData(encryptedData);
    //        setSecretKey(secretKey);
    //        setCryptoInitVector(initVector);
    //    }
    public SecretEntity(byte[] encryptedData, UserSecretKeyEntity secretKey,
            byte[] initVector, String cipherAlgorithm) {
        setEncryptedData(encryptedData, secretKey, initVector,
                cipherAlgorithm);
        //        setEncryptedData(encryptedData);
        //        setSecretKey(secretKey);
        //        setCipherInitVector(initVector);
        //        setCipherTransformation(cipherAlgorithm);
    }

    public SecretEntity(Secret<EntityClass> secret) {
        setData(secret.getData());
        setEncryptedData(secret.getEncryptedData());
        setId(secret.getId());
        setSecretKey(new UserSecretKeyEntity(secret.getSecretKey()));
        setVersion(secret.getVersion());
        setCipherTransformation(secret.getCipherTransformation());
        setCipherInitVectorBase64(secret.getCipherInitVectorBase64());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected Long getVersion() {
        return version;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    protected void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setData(EntityClass data) {
        this.data = data;
    }

    public EntityClass getData() {
        return data;
    }

    public void setSecretKey(UserSecretKeyEntity secretKey) {
        this.secretKey = secretKey;
    }

    public UserSecretKeyEntity getSecretKey() {
        return secretKey;
    }

    public byte[] getCipherInitVector() {
        return getCipherInitVectorBase64() == null
                || getCipherInitVectorBase64().isEmpty() ? null
                : Base64.getDecoder().decode(getCipherInitVectorBase64());
    }

    protected void setCipherInitVectorBase64(String cryptoInitVectorBase64) {
        this.cipherInitVectorBase64 = cryptoInitVectorBase64;
    }

    protected String getCipherInitVectorBase64() {
        return cipherInitVectorBase64;
    }

    private void setCipherInitVector(byte[] cryptoInitVector) {
        setCipherInitVectorBase64(cryptoInitVector == null ? null
                : Base64.getEncoder().encodeToString(cryptoInitVector));
    }

    protected void setCipherTransformation(String cipherAlgorithm) {
        this.cipherTransformation = cipherAlgorithm;
    }

    public String getCipherTransformation() {
        return cipherTransformation;
    }

    public void setEncryptedData(byte[] encryptedData, UserSecretKeyEntity secretKey,
            byte[] initVector, String cipherAlgorithm) {
        setEncryptedData(encryptedData);
        setSecretKey(secretKey);
        setCipherInitVector(initVector);
        setCipherTransformation(cipherAlgorithm);
    }

    public abstract Secret<EntityClass> toSecret();
}
