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

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import f18a14c09s.pscpm.general.services.IOUtil;

/**
 *
 * @author fjohnson
 */
public class X509Adapter {

    public static X509Certificate getX509Certificate(byte[] encoded)
            throws CertificateException {
        if (encoded == null
                || encoded.length < 1) {
            return null;
        } else {
            ByteArrayInputStream bais = null;
            try {
                bais
                        = new ByteArrayInputStream(encoded);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                return (X509Certificate) cf.generateCertificate(bais);
            } finally {
                IOUtil.closeSafely(bais);
            }
        }
    }
}
