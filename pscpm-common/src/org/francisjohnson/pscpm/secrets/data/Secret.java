package org.francisjohnson.pscpm.secrets.data;

import java.io.Serializable;

import java.util.Base64;


import org.francisjohnson.pscpm.general.data.Identifiable;
import org.francisjohnson.pscpm.security.data.UserSecretKey;


public abstract class Secret<EntityClass> implements Serializable,
                                                     Identifiable<Long> {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    private Long id;
    private Long version;
    private UserSecretKey secretKey;

    /**
     * Optional depending on the cryptographic algorithm used.  The base-64
     * encoded, initialization vector used to encrypt the data stored in
     * ENCRYPTED_DATA.
     */
    private String cipherInitVectorBase64;

    private byte[] encryptedData;

    /**
     * Identifies the cryptographic algorithm for which the symmetric key,
     * stored in ENCRYPTED_SYMMETRIC_KEY column, is used.  Example: AES.
     */
    private String cipherTransformation;
    private transient EntityClass data;

    public Secret() {
    }

    //    public Secret(byte[] encryptedData, UserSecretKey secretKey,
    //                  byte[] initVector) {
    //        setEncryptedData(encryptedData);
    //        setSecretKey(secretKey);
    //        setCryptoInitVector(initVector);
    //    }

    public Secret(byte[] encryptedData, UserSecretKey secretKey,
                  byte[] initVector, String cipherAlgorithm) {
        setEncryptedData(encryptedData, secretKey, initVector,
                         cipherAlgorithm);
        //        setEncryptedData(encryptedData);
        //        setSecretKey(secretKey);
        //        setCipherInitVector(initVector);
        //        setCipherTransformation(cipherAlgorithm);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setEncryptedData(byte[] encryptedData) {
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

    public void setSecretKey(UserSecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public UserSecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getCipherInitVector() {
        return getCipherInitVectorBase64() == null ||
               getCipherInitVectorBase64().isEmpty() ? null :
               Base64.getDecoder().decode(getCipherInitVectorBase64());
    }

    public void setCipherInitVectorBase64(String cryptoInitVectorBase64) {
        this.cipherInitVectorBase64 = cryptoInitVectorBase64;
    }

    private String getCipherInitVectorBase64() {
        return cipherInitVectorBase64;
    }

    private void setCipherInitVector(byte[] cryptoInitVector) {
        setCipherInitVectorBase64(cryptoInitVector == null ? null :
                                  Base64.getEncoder().encodeToString(cryptoInitVector));
    }

    public void setCipherTransformation(String cipherAlgorithm) {
        this.cipherTransformation = cipherAlgorithm;
    }

    public String getCipherTransformation() {
        return cipherTransformation;
    }

    public void setEncryptedData(byte[] encryptedData, UserSecretKey secretKey,
                                 byte[] initVector, String cipherAlgorithm) {
        setEncryptedData(encryptedData);
        setSecretKey(secretKey);
        setCipherInitVector(initVector);
        setCipherTransformation(cipherAlgorithm);
    }
}
