package org.francisjohnson.pscpm.security.services.javacrypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;


public class PublicKeyFingerprinter {
    public static final String DEFAULT_FINGERPRINT_ALGORITHM = "SHA-512";

    public static byte[] computePublicKeyFingerprint(PublicKey pubKey) throws NoSuchAlgorithmException {
        if (pubKey == null || pubKey.getEncoded() == null ||
            pubKey.getEncoded().length < 1) {
            return null;
        } else {
            MessageDigest digest =
                MessageDigest.getInstance(DEFAULT_FINGERPRINT_ALGORITHM);
            return digest.digest(pubKey.getEncoded());
        }
    }

    public static byte[] computePublicKeyFingerprint(byte[] pubKeyEncoded) throws NoSuchAlgorithmException {
        if (pubKeyEncoded == null || pubKeyEncoded.length < 1) {
            return null;
        } else {
            MessageDigest digest =
                MessageDigest.getInstance(DEFAULT_FINGERPRINT_ALGORITHM);
            return digest.digest(pubKeyEncoded);
        }
    }
}
