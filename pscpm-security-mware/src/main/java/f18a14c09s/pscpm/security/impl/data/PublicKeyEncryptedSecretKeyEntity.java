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
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import f18a14c09s.pscpm.general.data.Identifiable;
import f18a14c09s.pscpm.security.data.IPublicKeyEncryptedKey;
import f18a14c09s.pscpm.security.data.PublicKeyEncryptedSecretKey;
import f18a14c09s.pscpm.security.data.User;
import f18a14c09s.pscpm.security.services.javacrypto.PublicKeyFingerprinter;

/**
 * TODO: Ensure that the underlying database table is kept internal and not
 * readable. This is a way to prevent users from re-sharing secret keys shared
 * by others. TODO: See if any of the fields should move to UserSecretKey
 * entity.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "PublicKeyEncryptedSecretKeyEntity.findAll",
            query
            = "select o from PublicKeyEncryptedSecretKeyEntity o")
    ,@NamedQuery(name = "PublicKeyEncryptedSecretKeyEntity.findByOwner", query = "select o from PublicKeyEncryptedSecretKeyEntity o"
            + " where o.secretKey.owner = :owner")})
@SequenceGenerator(name = "PUB_KEY_ENCRYPTED_SECR_KEY_S",
        sequenceName = "PUB_KEY_ENCRYPTED_SECR_KEY_S",
        allocationSize = 1, initialValue = 1)
@Table(name = "PUBLIC_KEY_ENCRYPTED_SECR_KEY")
public class PublicKeyEncryptedSecretKeyEntity implements Serializable,
        Identifiable<Long>, IPublicKeyEncryptedKey {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "ID",
            precision = 38, scale = 0, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "PUB_KEY_ENCRYPTED_SECR_KEY_S")
    private Long id;

    /**
     * This is the owner of this encrypted copy of the secret key. This owner
     * does not decide whether to share the encrypted key with others.
     */
    @ManyToOne
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "ID",
            nullable = false
    //            ,precision=38,scale=0
    )
    private UserEntity owner;

    @ManyToOne
    @JoinColumn(name = "SECRET_KEY_ID", referencedColumnName = "ID",
            nullable = false
    //            ,precision=38,scale=0
    )
    private UserSecretKeyEntity secretKey;

    /**
     * Base-64 encoded, (asymmetric) public key fingerprint. Used to more
     * concisely identify the public key when matching its public-private key
     * pair.
     */
    @Column(name = "PUBLIC_KEY_FINGERPRINT",
            length = 1000, nullable = false)
    private String publicKeyFingerprintBase64;

    /**
     * Identifies the hashing algorithm that produced the public key fingerprint
     * stored in PUBLIC_KEY_FINGERPRINT column. Example: SHA-512.
     */
    @Column(name = "PUB_KEY_FPRINT_ALG_SHORT_NAME",
            length = 100, nullable = false)
    private String pubKeyFingerprintAlgorithm;

    /**
     * The full, base-64 encoded public key. Used to encrypt the symmetric key
     * stored in ENCRYPTED_SYMMETRIC_KEY.
     */
    @Column(name = "PUBLIC_KEY", nullable = false)
    @Lob
    private String publicKeyBase64;

    /**
     * Optional depending on the cryptographic algorithm used. The base-64
     * encoded, initialization vector used to encrypt the symmetric key stored
     * in ENCRYPTED_SYMMETRIC_KEY.
     */
    @Column(name = "CRYPTO_INITIALIZATION_VECTOR",
            length = 4000)
    private String cryptoInitVectorBase64;

    /**
     * The asymmetric algorithm used to encrypt the key contained herein.
     */
    @Column(name = "ASYMMETRIC_ALGORITHM",
            length = 100, nullable = false)
    private String asymmetricAlgorithm;

    /**
     * Implements the main purpose: stores the symmetric, secret key, encrypted
     * and encoded in base-64.
     */
    @Column(name = "ENCRYPTED_SYMMETRIC_KEY",
            length = 4000, nullable = false)
    private String encryptedSymmetricKeyBase64;

    public PublicKeyEncryptedSecretKeyEntity() {
    }

    //    public PublicKeyEncryptedSecretKey(String publicKeyFingerprintBase64,
    //                                       String pubKeyFingerprintAlgorithm,
    //                                       String publicKeyBase64,
    //                                       String cryptoInitVectorBase64,
    //                                       String encryptedSymmetricKeyBase64,
    //                                       String symmetricAlgorithm) {
    //        setPublicKeyFingerprintBase64(publicKeyFingerprintBase64);
    //        setPubKeyFingerprintAlgorithm(pubKeyFingerprintAlgorithm);
    //        setPublicKeyBase64(publicKeyBase64);
    //        setCryptoInitVectorBase64(cryptoInitVectorBase64);
    //        setEncryptedSymmetricKeyBase64(encryptedSymmetricKeyBase64);
    //        setSymmetricAlgorithm(symmetricAlgorithm);
    //    }
    //
    //    public PublicKeyEncryptedSecretKey(byte[] publicKeyFingerprint,
    //                                       String pubKeyFingerprintAlgorithm,
    //                                       byte[] publicKey,
    //                                       byte[] cryptoInitVector,
    //                                       String asymmetricAlgorithm,
    //                                       byte[] encryptedSymmetricKey,
    //                                       String symmetricAlgorithm) {
    //        setPublicKeyFingerprint(publicKeyFingerprint);
    //        setPubKeyFingerprintAlgorithm(pubKeyFingerprintAlgorithm);
    //        setPublicKey(publicKey);
    //        setCryptoInitVector(cryptoInitVector);
    //        setAsymmetricAlgorithm(asymmetricAlgorithm);
    //        setEncryptedSymmetricKey(encryptedSymmetricKey);
    //        setSymmetricAlgorithm(symmetricAlgorithm);
    //    }
    //    public PublicKeyEncryptedSecretKey(User owner, X509Certificate cryptoCert,
    //                                       byte[] cryptoInitVector,
    //                                       String asymmetricAlgorithm,
    //                                       byte[] encryptedSymmetricKey,
    //                                       String symmetricAlgorithm) throws CertificateException,
    //                                                                         NoSuchAlgorithmException {
    //        setOwner(owner);
    //        //        setPublicKeyFingerprint(owner.getCachedPublicKeyFingerprint());
    //        //        setPubKeyFingerprintAlgorithm(owner.getPublicKeyFingerprintAlgorithm());
    //        setPublicKeyFingerprint(FingerprintPrototype.computePublicKeyFingerprint(cryptoCert.getPublicKey()));
    //        setPubKeyFingerprintAlgorithm(FingerprintPrototype.DEFAULT_FINGERPRINT_ALGORITHM);
    //        setPublicKey(cryptoCert.getPublicKey().getEncoded());
    //        setCryptoInitVector(cryptoInitVector);
    //        setAsymmetricAlgorithm(asymmetricAlgorithm);
    //        setEncryptedSymmetricKey(encryptedSymmetricKey);
    //        setSymmetricAlgorithm(symmetricAlgorithm);
    //    }
    public PublicKeyEncryptedSecretKeyEntity(UserEntity owner, X509Certificate cryptoCert,
            byte[] cryptoInitVector,
            String asymmetricAlgorithm,
            byte[] encryptedSymmetricKey) throws CertificateException,
            NoSuchAlgorithmException {
        setOwner(owner);
        setPublicKeyFingerprint(PublicKeyFingerprinter.computePublicKeyFingerprint(cryptoCert.getPublicKey()));
        setPubKeyFingerprintAlgorithm(PublicKeyFingerprinter.DEFAULT_FINGERPRINT_ALGORITHM);
        setPublicKey(cryptoCert.getPublicKey().getEncoded());
        setCryptoInitVector(cryptoInitVector);
        setAsymmetricAlgorithm(asymmetricAlgorithm);
        setEncryptedSymmetricKey(encryptedSymmetricKey);
    }

    public PublicKeyEncryptedSecretKeyEntity(X509Certificate cryptoCert,
            byte[] cryptoInitVector,
            String asymmetricAlgorithm,
            byte[] encryptedSymmetricKey) throws CertificateException,
            NoSuchAlgorithmException {
        setPublicKeyFingerprint(PublicKeyFingerprinter.computePublicKeyFingerprint(cryptoCert.getPublicKey()));
        setPubKeyFingerprintAlgorithm(PublicKeyFingerprinter.DEFAULT_FINGERPRINT_ALGORITHM);
        setPublicKey(cryptoCert.getPublicKey().getEncoded());
        setCryptoInitVector(cryptoInitVector);
        setAsymmetricAlgorithm(asymmetricAlgorithm);
        setEncryptedSymmetricKey(encryptedSymmetricKey);
    }

    public PublicKeyEncryptedSecretKeyEntity(PublicKeyEncryptedSecretKey from, UserEntity owner, UserSecretKeyEntity secretKey) {
        setAsymmetricAlgorithm(from.getAsymmetricAlgorithm());
        setCryptoInitVectorBase64(from.getCryptoInitVectorBase64());
        setEncryptedSymmetricKeyBase64(from.getEncryptedSymmetricKeyBase64());
        setOwner(owner);
        setPubKeyFingerprintAlgorithm(from.getPubKeyFingerprintAlgorithm());
        setPublicKeyBase64(from.getPublicKeyBase64());
        setPublicKeyFingerprintBase64(from.getPublicKeyFingerprintBase64());
        setSecretKey(secretKey);
    }

    public PublicKeyEncryptedSecretKeyEntity(PublicKeyEncryptedSecretKey secretKey) {
        setAsymmetricAlgorithm(
                secretKey.getAsymmetricAlgorithm());
        setCryptoInitVectorBase64(
                secretKey.getCryptoInitVectorBase64());
        setEncryptedSymmetricKeyBase64(
                secretKey.getEncryptedSymmetricKeyBase64());
        setId(
                secretKey.getId());
        setOwner(secretKey.getOwner() == null ? null : new UserEntity(secretKey.getOwner()));
        setPubKeyFingerprintAlgorithm(
                secretKey.getPubKeyFingerprintAlgorithm());
        setPublicKeyBase64(
                secretKey.getPublicKeyBase64());
        setPublicKeyFingerprintBase64(
                secretKey.getPublicKeyFingerprintBase64());
        setSecretKey(secretKey.getSecretKey() == null ? null : new UserSecretKeyEntity(secretKey.getSecretKey()));
        secretKey.getSymmetricAlgorithm();
    }

    private void setPublicKeyFingerprintBase64(String publicKeyFingerprintBase64) {
        this.publicKeyFingerprintBase64 = publicKeyFingerprintBase64;
    }

    public String getPublicKeyFingerprintBase64() {
        return publicKeyFingerprintBase64;
    }

    private void setPubKeyFingerprintAlgorithm(String pubKeyFingerprintAlgorithm) {
        this.pubKeyFingerprintAlgorithm = pubKeyFingerprintAlgorithm;
    }

    public String getPubKeyFingerprintAlgorithm() {
        return pubKeyFingerprintAlgorithm;
    }

    private void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    private void setCryptoInitVectorBase64(String cryptoInitVectorBase64) {
        this.cryptoInitVectorBase64 = cryptoInitVectorBase64;
    }

    public String getCryptoInitVectorBase64() {
        return cryptoInitVectorBase64;
    }

    private void setEncryptedSymmetricKeyBase64(String encryptedSymmetricKeyBase64) {
        this.encryptedSymmetricKeyBase64 = encryptedSymmetricKeyBase64;
    }

    public String getEncryptedSymmetricKeyBase64() {
        return encryptedSymmetricKeyBase64;
    }

    //    private void setSymmetricAlgorithm(String symmetricAlgorithm) {
    //        this.symmetricAlgorithm = symmetricAlgorithm;
    //    }
    public String getSymmetricAlgorithm() {
        return getSecretKey() == null ? null
                : getSecretKey().getSymmetricAlgorithm();
    }

    public byte[] getCryptoInitVector() {
        return getCryptoInitVectorBase64() == null
                || getCryptoInitVectorBase64().isEmpty() ? null
                : Base64.getDecoder().decode(getCryptoInitVectorBase64());
    }

    public byte[] getEncryptedSymmetricKey() {
        return getEncryptedSymmetricKeyBase64() == null
                || getEncryptedSymmetricKeyBase64().isEmpty() ? null
                : Base64.getDecoder().decode(getEncryptedSymmetricKeyBase64());
    }

    public byte[] getPublicKey() {
        return getPublicKeyBase64() == null || getPublicKeyBase64().isEmpty()
                ? null : Base64.getDecoder().decode(getPublicKeyBase64());
    }

    public byte[] getPublicKeyFingerprint() {
        return getPublicKeyFingerprintBase64() == null
                || getPublicKeyFingerprintBase64().isEmpty() ? null
                : Base64.getDecoder().decode(getPublicKeyFingerprintBase64());
    }

    private void setCryptoInitVector(byte[] cryptoInitVector) {
        setCryptoInitVectorBase64(cryptoInitVector == null ? null
                : Base64.getEncoder().encodeToString(cryptoInitVector));
    }

    private void setEncryptedSymmetricKey(byte[] encryptedSymmetricKey) {
        setEncryptedSymmetricKeyBase64(encryptedSymmetricKey == null ? null
                : Base64.getEncoder().encodeToString(encryptedSymmetricKey));
    }

    private void setPublicKey(byte[] publicKey) {
        setPublicKeyBase64(publicKey == null ? null
                : Base64.getEncoder().encodeToString(publicKey));
    }

    private void setPublicKeyFingerprint(byte[] publicKeyFingerprint) {
        setPublicKeyFingerprintBase64(publicKeyFingerprint == null ? null
                : Base64.getEncoder().encodeToString(publicKeyFingerprint));
    }

    private void setAsymmetricAlgorithm(String asymmetricAlgorithm) {
        this.asymmetricAlgorithm = asymmetricAlgorithm;
    }

    public String getAsymmetricAlgorithm() {
        return asymmetricAlgorithm;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setSecretKey(UserSecretKeyEntity secretKey) {
        this.secretKey = secretKey;
    }

    public UserSecretKeyEntity getSecretKey() {
        return secretKey;
    }

    public PublicKeyEncryptedSecretKey toPublicKeyEncryptedSecretKey() {
        PublicKeyEncryptedSecretKey retval = new PublicKeyEncryptedSecretKey();
        retval.setAsymmetricAlgorithm(
                getAsymmetricAlgorithm());
        retval.setCryptoInitVectorBase64(
                getCryptoInitVectorBase64());
        retval.setEncryptedSymmetricKeyBase64(
                getEncryptedSymmetricKeyBase64());
        retval.setId(
                getId());
        retval.setOwner(getOwner() == null ? null : getOwner().toUser());
        retval.setPubKeyFingerprintAlgorithm(
                getPubKeyFingerprintAlgorithm());
        retval.setPublicKeyBase64(
                getPublicKeyBase64());
        retval.setPublicKeyFingerprintBase64(
                getPublicKeyFingerprintBase64());
        retval.setSecretKey(getSecretKey() == null ? null : getSecretKey().toUserSecretKey());
        getSymmetricAlgorithm();
        return retval;
    }

    public PublicKeyEncryptedSecretKey toPublicKeyEncryptedSecretKey(User owner) {
        PublicKeyEncryptedSecretKey retval = new PublicKeyEncryptedSecretKey();
        retval.setAsymmetricAlgorithm(
                getAsymmetricAlgorithm());
        retval.setCryptoInitVectorBase64(
                getCryptoInitVectorBase64());
        retval.setEncryptedSymmetricKeyBase64(
                getEncryptedSymmetricKeyBase64());
        retval.setId(
                getId());
        retval.setOwner(owner);
        retval.setPubKeyFingerprintAlgorithm(
                getPubKeyFingerprintAlgorithm());
        retval.setPublicKeyBase64(
                getPublicKeyBase64());
        retval.setPublicKeyFingerprintBase64(
                getPublicKeyFingerprintBase64());
        retval.setSecretKey(getSecretKey() == null ? null : getSecretKey().toUserSecretKey());
        getSymmetricAlgorithm();
        return retval;
    }
}
