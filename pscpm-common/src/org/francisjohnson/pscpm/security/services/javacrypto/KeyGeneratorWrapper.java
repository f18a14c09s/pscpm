package org.francisjohnson.pscpm.security.services.javacrypto;

import java.io.IOException;

import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;


public class KeyGeneratorWrapper {
    public static final String DEFAULT_CRYPTOGRAPHIC_ALGORITHM = "AES";
    public static final int DEFAULT_CRYPTOGRAPHIC_KEY_SIZE = 256;

    public KeyGeneratorWrapper() {
        super();
    }

    public static void main(String... args) {
        try {
            Map<String, UserCredential> credentials = IdentityKeyStoreAdapter.filterPrivateKeys("Francis Johnson");
            for (UserCredential cred : credentials.values()) {
                System.out.println("Alias " + cred.getAlias() + " found.");
            }
            UserCredential cred = IdentityKeyStoreAdapter.getBasicEncryptionCredential("Francis Johnson");
            System.out.println("Using alias " + cred.getAlias() + ".");
            System.out.println("\tAsymmetric encryption algorithm: " +
                               cred.getKey().getAlgorithm() + ".");

            PublicKeyEncryptedSecretKey symmetricKey = KeyEncryptionAdapter.encrypt(generateSecretKey(),
                                                     new User("business@francisjohnson.org",
                                                              cred.getCert()),
                                                     cred.getCert());
        } catch (KeyStoreException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(3);
        } catch (CertificateException e) {
            e.printStackTrace();
            System.exit(4);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            System.exit(5);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            System.exit(6);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            System.exit(7);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            System.exit(8);
        } catch (BadPaddingException e) {
            e.printStackTrace();
            System.exit(9);
        }
    }

    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen =
            KeyGenerator.getInstance(DEFAULT_CRYPTOGRAPHIC_ALGORITHM);
        keyGen.init(DEFAULT_CRYPTOGRAPHIC_KEY_SIZE);
        return keyGen.generateKey();
    }
}
