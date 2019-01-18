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

import org.francisjohnson.pscpm.security.services.javacrypto.PublicKeyFingerprinter;


/**
 * This is the non-JPA-entity version of PublicKeyEncryptedSecretKey, used for
 * general-purpose keys such as passwords.
 */
public class PublicKeyEncryptedKey implements Serializable,
                                              IPublicKeyEncryptedKey {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    /**
     * The full, base-64 encoded public key.  Used to encrypt the symmetric key
     * stored in encryptedSymmetricKeyBase64.
     */
    private String publicKeyBase64;

    /**
     * The asymmetric algorithm used to encrypt the key contained herein.
     */
    private String asymmetricAlgorithm;

    /**
     * Optional depending on the cryptographic algorithm used.  The base-64
     * encoded, initialization vector used to encrypt the symmetric key stored
     * in encryptedSymmetricKeyBase64.
     */
    private String cryptoInitVectorBase64;

    /**
     * Implements the main purpose: stores the symmetric, secret key, encrypted
     * and encoded in base-64.
     */
    private String encryptedSymmetricKeyBase64;

    public PublicKeyEncryptedKey() {
    }

    public PublicKeyEncryptedKey(X509Certificate cryptoCert,
                                 byte[] cryptoInitVector,
                                 String asymmetricAlgorithm,
                                 byte[] encryptedSymmetricKey) throws CertificateException,
                                                                      NoSuchAlgorithmException {
        setPublicKey(cryptoCert.getPublicKey().getEncoded());
        setCryptoInitVector(cryptoInitVector);
        setAsymmetricAlgorithm(asymmetricAlgorithm);
        setEncryptedSymmetricKey(encryptedSymmetricKey);
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

    public byte[] getCryptoInitVector() {
        return getCryptoInitVectorBase64() == null ||
               getCryptoInitVectorBase64().isEmpty() ? null :
               Base64.getDecoder().decode(getCryptoInitVectorBase64());
    }

    private void setCryptoInitVector(byte[] cryptoInitVector) {
        setCryptoInitVectorBase64(cryptoInitVector == null ? null :
                                  Base64.getEncoder().encodeToString(cryptoInitVector));
    }

    public byte[] getEncryptedSymmetricKey() {
        return getEncryptedSymmetricKeyBase64() == null ||
               getEncryptedSymmetricKeyBase64().isEmpty() ? null :
               Base64.getDecoder().decode(getEncryptedSymmetricKeyBase64());
    }

    private void setEncryptedSymmetricKey(byte[] encryptedSymmetricKey) {
        setEncryptedSymmetricKeyBase64(encryptedSymmetricKey == null ? null :
                                       Base64.getEncoder().encodeToString(encryptedSymmetricKey));
    }

    public byte[] getPublicKey() {
        return getPublicKeyBase64() == null || getPublicKeyBase64().isEmpty() ?
               null : Base64.getDecoder().decode(getPublicKeyBase64());
    }

    private void setPublicKey(byte[] publicKey) {
        setPublicKeyBase64(publicKey == null ? null :
                           Base64.getEncoder().encodeToString(publicKey));
    }

    public String getPublicKeyFingerprintBase64() throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(PublicKeyFingerprinter.computePublicKeyFingerprint(getPublicKey()));
    }

    public String getPubKeyFingerprintAlgorithm() {
        return PublicKeyFingerprinter.DEFAULT_FINGERPRINT_ALGORITHM;
    }

    public String getAsymmetricAlgorithm() {
        return asymmetricAlgorithm;
    }

    private void setAsymmetricAlgorithm(String asymmetricAlgorithm) {
        this.asymmetricAlgorithm = asymmetricAlgorithm;
    }
}
