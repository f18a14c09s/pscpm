package org.francisjohnson.pscpm.security.data;

import java.security.NoSuchAlgorithmException;


public interface IPublicKeyEncryptedKey {
    String getPublicKeyBase64();

    byte[] getPublicKey();

    String getAsymmetricAlgorithm();

    String getCryptoInitVectorBase64();

    byte[] getCryptoInitVector();

    String getEncryptedSymmetricKeyBase64();

    byte[] getEncryptedSymmetricKey();

    String getPublicKeyFingerprintBase64() throws NoSuchAlgorithmException;

    String getPubKeyFingerprintAlgorithm();
}
