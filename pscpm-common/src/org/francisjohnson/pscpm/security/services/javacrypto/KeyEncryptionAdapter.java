package org.francisjohnson.pscpm.security.services.javacrypto;

import java.io.IOException;

import java.nio.ByteBuffer;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedKey;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.User;


public class KeyEncryptionAdapter {
    public static final String DEFAULT_CIPHER_TRANSFORMATION =
        "RSA/ECB/PKCS1Padding";

    public static PublicKeyEncryptedSecretKey encrypt(SecretKey symmetricKey,
                                                      User owner,
                                                      X509Certificate cryptoCert) throws NoSuchAlgorithmException,
                                                                                         NoSuchPaddingException,
                                                                                         InvalidKeyException,
                                                                                         IllegalBlockSizeException,
                                                                                         BadPaddingException,
                                                                                         CertificateException {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, cryptoCert.getPublicKey());
        return new PublicKeyEncryptedSecretKey(owner, cryptoCert,
                                               cipher.getIV(),
                                               cipher.getAlgorithm(),
                                               cipher.doFinal(symmetricKey.getEncoded()));
    }

    /**
     * @deprecated Because the system supports sharing and one cannot share a
     * public-key-encrypted password without sharing the private key.
     * @param password
     * @param cryptoCert
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws CertificateException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Deprecated
    public static PublicKeyEncryptedKey encrypt(char[] password,
                                                X509Certificate cryptoCert) throws NoSuchAlgorithmException,
                                                                                   NoSuchPaddingException,
                                                                                   InvalidKeyException,
                                                                                   IllegalBlockSizeException,
                                                                                   BadPaddingException,
                                                                                   CertificateException,
                                                                                   IOException {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, cryptoCert.getPublicKey());
        ByteBuffer buf =
            ByteBuffer.allocate(Character.BYTES * password.length);
        for (char c : password) {
            buf.putChar(c);
        }
        byte[] retval = cipher.doFinal(buf.array());
        Arrays.fill(buf.array(), (byte)0);
        return new PublicKeyEncryptedKey(cryptoCert, cipher.getIV(),
                                         cipher.getAlgorithm(), retval);
    }
}
