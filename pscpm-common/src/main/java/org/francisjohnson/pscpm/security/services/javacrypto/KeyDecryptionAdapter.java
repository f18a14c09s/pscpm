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
package org.francisjohnson.pscpm.security.services.javacrypto;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import java.nio.ByteBuffer;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.francisjohnson.pscpm.security.data.IPublicKeyEncryptedKey;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;


public class KeyDecryptionAdapter {
    public static void main(String[] args) {
        try {
            Map<String, UserCredential> credentials = IdentityKeyStoreAdapter.filterPrivateKeys(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
            for (int i = 0; i < 4; i++) {
                SecretKey sourceSecretKey = KeyGeneratorWrapper.generateSecretKey();
                UserCredential sigCred = IdentityKeyStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
                UserCredential cryptCred = IdentityKeyStoreAdapter.getBasicEncryptionCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
                PublicKeyEncryptedSecretKey encryptedKey = KeyEncryptionAdapter.encrypt(sourceSecretKey,
                                                   new User(sigCred.getRfc822SubjectAlternativeName(),
                                                            sigCred.getCert()),
                                                   cryptCred.getCert());
                //
                UserCredential decryptionPrivateKey = null;
                for (UserCredential cred : credentials.values()) {
                    String fingerprint =
                        cred.getPublicKeyFingerprintBase64(encryptedKey.getPubKeyFingerprintAlgorithm());
                    if (fingerprint.equals(encryptedKey.getPublicKeyFingerprintBase64())) {
                        decryptionPrivateKey = cred;
                        break;
                    }
                }
                SecretKey decryptedSecretKey = decrypt(encryptedKey);
                //                            , decryptionPrivateKey
                getLog().info("Source secret key: " +
                                   Base64.getEncoder().encodeToString(sourceSecretKey.getEncoded()));
                getLog().info("E-then-D secret key: " +
                                   Base64.getEncoder().encodeToString(decryptedSecretKey.getEncoded()));
                getLog().info("Source and encrypted-then-decrypted key match? " +
                                   Arrays.equals(sourceSecretKey.getEncoded(),
                                                 decryptedSecretKey.getEncoded()) +
                                   ".");
            }
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (CertificateException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(3);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(4);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            System.exit(5);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            System.exit(6);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            System.exit(7);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            System.exit(8);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            System.exit(9);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            System.exit(10);
        }
    }

    private static UserCredential findPrivateKey(IPublicKeyEncryptedKey encryptedKey) throws KeyStoreException,
                                                                                             IOException,
                                                                                             NoSuchAlgorithmException,
                                                                                             CertificateException,
                                                                                             UnrecoverableKeyException {
        Map<String, UserCredential> privateKeys = IdentityKeyStoreAdapter.filterPrivateKeys(null);
        for (UserCredential cred : privateKeys.values()) {
            if (cred.getPublicKeyFingerprintBase64(encryptedKey.getPubKeyFingerprintAlgorithm()).equals(encryptedKey.getPublicKeyFingerprintBase64())) {
                return cred;
            }
        }
        return null;
    }

    public static SecretKey decrypt(PublicKeyEncryptedSecretKey encryptedKey) throws NoSuchAlgorithmException,
                                                                                     NoSuchPaddingException,
                                                                                     InvalidKeyException,
                                                                                     InvalidAlgorithmParameterException,
                                                                                     IllegalBlockSizeException,
                                                                                     BadPaddingException,
                                                                                     KeyStoreException,
                                                                                     IOException,
                                                                                     CertificateException,
                                                                                     UnrecoverableKeyException {
        // This seems to be a legitimate purpose for not filtering the list of
        // private keys.  The goal is to match the public key with the correct
        // private key, so if you filter here it the matching could fail.
        UserCredential credential = findPrivateKey(encryptedKey);
        // TODO: Replace this forced NPE with proper checking.
        credential.toString();
        Cipher cipher =
            Cipher.getInstance(encryptedKey.getAsymmetricAlgorithm(),
                //with.getKey().getAlgorithm()
                credential.getProvider());
        byte[] initVector = encryptedKey.getCryptoInitVector();
        cipher.init(Cipher.DECRYPT_MODE, credential.getKey(),
                    initVector == null ? null :
                    new IvParameterSpec(initVector));
        /* java.security.KeyException "The system cannot find the file specified,"
         * can occur here when there is a mismatch between the (public)
         * encryption key (or its fingerprint) used and decryption key (or its
         * corresponding public key's fingerprint) provided.  This can occur if
         * this application has a bug that uses the smart card's basic encryption
         * certificate to encrypt the secret key and the advanced signature
         * certificate to decrypt, for example. */
        byte[] decryptedKey =
            cipher.doFinal(encryptedKey.getEncryptedSymmetricKey());
        return new SecretKeySpec(decryptedKey, 0, decryptedKey.length,
                                 encryptedKey.getSymmetricAlgorithm());
    }

    /**
     * @deprecated Because the system supports sharing and one cannot share a
     * public-key-encrypted password without sharing the private key.
     * @param encryptedKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws CertificateException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws InvalidAlgorithmParameterException
     */
    @Deprecated
    public static char[] decryptPassword(IPublicKeyEncryptedKey encryptedKey) throws NoSuchAlgorithmException,
                                                                                     NoSuchPaddingException,
                                                                                     InvalidKeyException,
                                                                                     IllegalBlockSizeException,
                                                                                     BadPaddingException,
                                                                                     CertificateException,
                                                                                     IOException,
                                                                                     ClassNotFoundException,
                                                                                     KeyStoreException,
                                                                                     UnrecoverableKeyException,
                                                                                     InvalidAlgorithmParameterException {
        UserCredential credential = findPrivateKey(encryptedKey);
        Cipher cipher =
            Cipher.getInstance(encryptedKey.getAsymmetricAlgorithm(),
                               credential.getProvider());
        byte[] initVector = encryptedKey.getCryptoInitVector();
        cipher.init(Cipher.DECRYPT_MODE, credential.getKey(),
                    initVector == null ? null :
                    new IvParameterSpec(initVector));
        /* java.security.KeyException "The system cannot find the file specified,"
         * can occur here when there is a mismatch between the (public)
         * encryption key (or its fingerprint) used and decryption key (or its
         * corresponding public key's fingerprint) provided.  This can occur if
         * this application has a bug that uses the smart card's basic encryption
         * certificate to encrypt the secret key and the advanced signature
         * certificate to decrypt, for example. */
        byte[] decryptedKey =
            cipher.doFinal(encryptedKey.getEncryptedSymmetricKey());
        ByteBuffer buf = ByteBuffer.wrap(decryptedKey);
        char[] retval = new char[decryptedKey.length / Character.BYTES];
        for (int i = 0; i < retval.length; i++) {
            retval[i] = buf.getChar();
        }
        Arrays.fill(decryptedKey, (byte)0);
        return retval;
    }

    private static Logger getLog() {
        return Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    }
}
