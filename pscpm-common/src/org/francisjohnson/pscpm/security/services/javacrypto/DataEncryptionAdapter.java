package org.francisjohnson.pscpm.security.services.javacrypto;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.general.services.IOUtil;


public class DataEncryptionAdapter {
    public static final String DEFAULT_CRYPTOGRAPHIC_TRANSFORMATION =
        "AES/CTR/PKCS5Padding";

    public static CipherData encrypt(Serializable data,
                                     PublicKeyEncryptedSecretKey secretKey) throws Exception {
        Task task = null;
        ObjectOutputStream oos = null;
        try {
            task = setupStreams(secretKey);
            oos = new ObjectOutputStream(task.get(0));
            oos.writeObject(data);
        } finally {
            IOUtil.closeSafely(oos);
            IOUtil.closeSafely(task);
        }
        // TODO: Find an alternative, as "The default implementation throws
        // DestroyFailedException."
        //        try {
        //            outputStreams.getSecretKey().destroy();
        //        } catch (DestroyFailedException e) {
        //            e.printStackTrace();
        //        }
        return new CipherData(task.getBaos().toByteArray(),
                              task.getCipher().getIV(),
                              task.getCipher().getAlgorithm());
    }

    public static final class CipherData {
        private byte[] data;
        private byte[] initVector;
        private String transformation;

        public CipherData(byte[] data, byte[] initVector,
                          String transformation) {
            setData(data);
            setInitVector(initVector);
            setTransformation(transformation);
        }

        private void setData(byte[] data) {
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }

        private void setInitVector(byte[] initVector) {
            this.initVector = initVector;
        }

        public byte[] getInitVector() {
            return initVector;
        }

        private void setTransformation(String transformation) {
            this.transformation = transformation;
        }

        public String getTransformation() {
            return transformation;
        }
    }

    private static final class Task extends ArrayList<OutputStream> {
        private transient Cipher cipher;
        private SecretKey secretKey;
        private transient ByteArrayOutputStream baos;

        private void setSecretKey(SecretKey secretKey) {
            this.secretKey = secretKey;
        }

        private SecretKey getSecretKey() {
            return secretKey;
        }

        private void setBaos(ByteArrayOutputStream baos) {
            this.baos = baos;
            super.add(0, baos);
        }

        private ByteArrayOutputStream getBaos() {
            return baos;
        }

        private void setCipher(Cipher cipher) {
            this.cipher = cipher;
        }

        private Cipher getCipher() {
            return cipher;
        }
    }

    private static Task setupStreams(PublicKeyEncryptedSecretKey encryptedKey) throws Exception {
        Task retval = new Task();
        retval.setSecretKey(KeyDecryptionAdapter.decrypt(encryptedKey));
        retval.setCipher(Cipher.getInstance(DEFAULT_CRYPTOGRAPHIC_TRANSFORMATION));
        //encryptedKey.getSymmetricAlgorithm()
        retval.getCipher().init(Cipher.ENCRYPT_MODE, retval.getSecretKey());
        try {
            retval.setBaos(new ByteArrayOutputStream());
            retval.add(0,
                       new CipherOutputStream(retval.get(0), retval.getCipher()));
        } catch (Exception e) {
            IOUtil.closeSafely(retval);
            throw e;
        }
        return retval;
    }
}
