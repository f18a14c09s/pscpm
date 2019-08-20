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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.francisjohnson.pscpm.secrets.data.Secret;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.general.services.IOUtil;


public class DataDecryptionAdapter {
    public static <EntityClass> EntityClass decrypt(Secret<EntityClass> secret,
                                                    PublicKeyEncryptedSecretKey encryptedKey) throws Exception {
        Task task = null;
        ObjectInputStream ois = null;
        try {
            task =
setupStreams(secret, KeyDecryptionAdapter.decrypt(encryptedKey));
            ois = new ObjectInputStream(task.get(0));
            return (EntityClass)ois.readObject();
        } finally {
            IOUtil.closeSafely(ois);
            IOUtil.closeSafely(task);
            // TODO: Find an alternative, as "The default implementation throws
            // DestroyFailedException."
            //        try {
            //            outputStreams.getSecretKey().destroy();
            //        } catch (DestroyFailedException e) {
            //            e.printStackTrace();
            //        }
        }
    }

    public static <EntityClass> List<Secret<EntityClass>> decrypt(List<Secret<EntityClass>> secrets,
                                                                  PublicKeyEncryptedSecretKey encryptedKey) throws Exception {
        //        List<EntityClass> retval = new ArrayList<EntityClass>();
        Task task = null;
        ObjectInputStream ois = null;
        SecretKey secretKey = KeyDecryptionAdapter.decrypt(encryptedKey);
        for (Secret<EntityClass> secret : secrets) {
            try {
                task = setupStreams(secret, secretKey);
                ois = new ObjectInputStream(task.get(0));
                secret.setData((EntityClass)ois.readObject());
            } finally {
                IOUtil.closeSafely(ois);
                IOUtil.closeSafely(task);
                // TODO: Find an alternative, as "The default implementation throws
                // DestroyFailedException."
                //        try {
                //            outputStreams.getSecretKey().destroy();
                //        } catch (DestroyFailedException e) {
                //            e.printStackTrace();
                //        }
            }
        }
        return secrets;
    }

    private static final class Task extends ArrayList<InputStream> {
        private transient Cipher cipher;
        //        private SecretKey secretKey;
        private transient ByteArrayInputStream bais;

        //        private void setSecretKey(SecretKey secretKey) {
        //            this.secretKey = secretKey;
        //        }
        //
        //        private SecretKey getSecretKey() {
        //            return secretKey;
        //        }

        private void setBais(ByteArrayInputStream bais) {
            this.bais = bais;
            super.add(0, bais);
        }

        private ByteArrayInputStream getBais() {
            return bais;
        }

        private void setCipher(Cipher cipher) {
            this.cipher = cipher;
        }

        private Cipher getCipher() {
            return cipher;
        }
    }

    private static Task setupStreams(Secret<?> secret, SecretKey secretKey)
        //                                     PublicKeyEncryptedSecretKey encryptedKey
        throws Exception {
        Task retval = new Task();
        retval.setCipher(Cipher.getInstance(secret.getCipherTransformation()));
        //encryptedKey.getSymmetricAlgorithm()
        retval.getCipher().init(Cipher.DECRYPT_MODE, secretKey,
                                secret.getCipherInitVector() == null ? null :
                                new IvParameterSpec(secret.getCipherInitVector()));
        try {
            retval.setBais(new ByteArrayInputStream(secret.getEncryptedData()));
            retval.add(0,
                       new CipherInputStream(retval.get(0), retval.getCipher()));
        } catch (Exception e) {
            IOUtil.closeSafely(retval);
            throw e;
        }
        return retval;
    }
}
