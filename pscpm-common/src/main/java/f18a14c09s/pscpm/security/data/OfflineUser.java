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

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;


public class OfflineUser extends User {
    private static final String DEFAULT_USER_ID = "Offline User";

    public OfflineUser() {
        super();
        setUserId(DEFAULT_USER_ID);
    }

    public OfflineUser(X509Certificate cert) throws NoSuchAlgorithmException,
                                                    CertificateEncodingException {
        super();
        setUserId(DEFAULT_USER_ID);
        setX509Certificate(cert);
    }
}
