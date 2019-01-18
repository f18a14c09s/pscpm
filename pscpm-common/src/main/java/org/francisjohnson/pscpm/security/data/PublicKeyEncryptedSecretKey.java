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
package org.francisjohnson.pscpm.security.data;

import java.io.Serializable;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Base64;

import org.francisjohnson.pscpm.general.data.Identifiable;
import org.francisjohnson.pscpm.security.services.javacrypto.PublicKeyFingerprinter;

/**
 * TODO: Ensure that the underlying database table is kept internal and not
 * readable. This is a way to prevent users from re-sharing secret keys shared
 * by others. TODO: See if any of the fields should move to UserSecretKey
 * entity.
 */
public class PublicKeyEncryptedSecretKey implements Serializable,
        Identifiable<Long>, IPublicKeyEncryptedKey {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    private Long id;

    /**
     * This is the owner of this encrypted copy of the secret key. This owner
     * does not decide whether to share the encrypted key with others.
     */
    private User owner;

    private UserSecretKey secretKey;

    /**
     * Base-64 encoded, (asymmetric) public key fingerprint. Used to more
     * concisely identify the public key when matching its public-private key
     * pair.
     */
    private String publicKeyFingerprintBase64;

    /**
     * Identifies the hashing algorithm that produced the public key fingerprint
     * stored in PUBLIC_KEY_FINGERPRINT column. Example: SHA-512.
     */
    private String pubKeyFingerprintAlgorithm;

    /**
     * The full, base-64 encoded public key. Used to encrypt the symmetric key
     * stored in ENCRYPTED_SYMMETRIC_KEY.
     */
    private String publicKeyBase64;

    /**
     * Optional depending on the cryptographic algorithm used. The base-64
     * encoded, initialization vector used to encrypt the symmetric key stored
     * in ENCRYPTED_SYMMETRIC_KEY.
     */
    private String cryptoInitVectorBase64;

    /**
     * The asymmetric algorithm used to encrypt the key contained herein.
     */
    private String asymmetricAlgorithm;

    /**
     * Implements the main purpose: stores the symmetric, secret key, encrypted
     * and encoded in base-64.
     */
    private String encryptedSymmetricKeyBase64;

    public PublicKeyEncryptedSecretKey() {
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
    public PublicKeyEncryptedSecretKey(User owner, X509Certificate cryptoCert,
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

    public PublicKeyEncryptedSecretKey(X509Certificate cryptoCert,
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

    public void setPublicKeyFingerprintBase64(String publicKeyFingerprintBase64) {
        this.publicKeyFingerprintBase64 = publicKeyFingerprintBase64;
    }

    public String getPublicKeyFingerprintBase64() {
        return publicKeyFingerprintBase64;
    }

    public void setPubKeyFingerprintAlgorithm(String pubKeyFingerprintAlgorithm) {
        this.pubKeyFingerprintAlgorithm = pubKeyFingerprintAlgorithm;
    }

    public String getPubKeyFingerprintAlgorithm() {
        return pubKeyFingerprintAlgorithm;
    }

    public void setPublicKeyBase64(String publicKeyBase64) {
        this.publicKeyBase64 = publicKeyBase64;
    }

    public String getPublicKeyBase64() {
        return publicKeyBase64;
    }

    public void setCryptoInitVectorBase64(String cryptoInitVectorBase64) {
        this.cryptoInitVectorBase64 = cryptoInitVectorBase64;
    }

    public String getCryptoInitVectorBase64() {
        return cryptoInitVectorBase64;
    }

    public void setEncryptedSymmetricKeyBase64(String encryptedSymmetricKeyBase64) {
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

    public void setAsymmetricAlgorithm(String asymmetricAlgorithm) {
        this.asymmetricAlgorithm = asymmetricAlgorithm;
    }

    public String getAsymmetricAlgorithm() {
        return asymmetricAlgorithm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void setSecretKey(UserSecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public UserSecretKey getSecretKey() {
        return secretKey;
    }

    @Override
    public String toString() {
        return String.format("%s (ID=%s)", getClass().getSimpleName(), getId());
    }
}
