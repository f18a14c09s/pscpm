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
package f18a14c09s.pscpm.security.data;

import java.io.Serializable;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class User extends SecurityPrincipal implements Serializable {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    private PublicKeyEncryptedSecretKey defaultSecretKey;

    public User() {
    }

    //    public User(X509Certificate cert) throws CertificateEncodingException,
    //                                             NoSuchAlgorithmException {
    //        super(cert);
    //    }

    public User(String userId,
                X509Certificate cert) throws CertificateEncodingException,
                                             NoSuchAlgorithmException {
        super(userId, cert);
    }

    public void setDefaultSecretKey(PublicKeyEncryptedSecretKey defaultSecretKey) {
        this.defaultSecretKey = defaultSecretKey;
    }

    public PublicKeyEncryptedSecretKey getDefaultSecretKey() {
        return defaultSecretKey;
    }
}
