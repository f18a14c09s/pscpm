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
package org.francisjohnson.pscpm.secrets.data;

import java.io.Serializable;


import org.francisjohnson.pscpm.security.data.UserSecretKey;


public class CertificateSecret extends Secret<Certificate> implements Serializable {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public CertificateSecret() {
    }

    public CertificateSecret(byte[] encryptedData, UserSecretKey secretKey,
                             byte[] initVector, String cipherAlgorithm) {
        super(encryptedData, secretKey, initVector, cipherAlgorithm);
    }

    public Certificate getCert() {
        return super.getData();
    }
}
