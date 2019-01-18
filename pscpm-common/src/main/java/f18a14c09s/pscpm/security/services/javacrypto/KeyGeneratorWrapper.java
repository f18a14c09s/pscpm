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
package f18a14c09s.pscpm.security.services.javacrypto;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import f18a14c09s.pscpm.security.data.PublicKeyEncryptedSecretKey;
import f18a14c09s.pscpm.security.data.User;
import f18a14c09s.pscpm.security.data.javacrypto.UserCredential;

public class KeyGeneratorWrapper {

    public static final String DEFAULT_CRYPTOGRAPHIC_ALGORITHM = "AES";
    public static final int DEFAULT_CRYPTOGRAPHIC_KEY_SIZE = 256;

    public KeyGeneratorWrapper() {
        super();
    }

    public static void main(String... args) {
        try {
            Map<String, UserCredential> credentials = IdentityKeyStoreAdapter.filterPrivateKeys(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
            for (UserCredential cred : credentials.values()) {
                getLog().info("Alias " + cred.getAlias() + " found.");
            }
            UserCredential cred = IdentityKeyStoreAdapter.getBasicEncryptionCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
            getLog().info("Using alias " + cred.getAlias() + ".");
            getLog().info("\tAsymmetric encryption algorithm: "
                    + cred.getKey().getAlgorithm() + ".");

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
        KeyGenerator keyGen
                = KeyGenerator.getInstance(DEFAULT_CRYPTOGRAPHIC_ALGORITHM);
        keyGen.init(DEFAULT_CRYPTOGRAPHIC_KEY_SIZE);
        return keyGen.generateKey();
    }

    private static Logger getLog() {
        return Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    }
}
