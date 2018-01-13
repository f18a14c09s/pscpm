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
