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
package f18a14c09s.pscpm.secrets.impl.data;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import f18a14c09s.pscpm.secrets.data.Certificate;
import f18a14c09s.pscpm.secrets.data.CertificateSecret;
import f18a14c09s.pscpm.secrets.data.Secret;

import f18a14c09s.pscpm.security.impl.data.UserSecretKeyEntity;

@Entity
@NamedQueries({
    @NamedQuery(name = "CertificateSecretEntity.findAll",
            query = "select o from CertificateSecretEntity o")})
@Inheritance
@DiscriminatorValue("CERTIFICATE")
public class CertificateSecretEntity extends SecretEntity<Certificate> implements Serializable {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public CertificateSecretEntity() {
    }

    public CertificateSecretEntity(Secret<Certificate> secret) {
        super(secret);
    }

    public CertificateSecretEntity(byte[] encryptedData, UserSecretKeyEntity secretKey,
            byte[] initVector, String cipherAlgorithm) {
        super(encryptedData, secretKey, initVector, cipherAlgorithm);
    }

    public Certificate getCert() {
        return super.getData();
    }

    @Override
    public CertificateSecret toSecret() {
        CertificateSecret retval = new CertificateSecret();
        retval.setCipherInitVectorBase64(
                getCipherInitVectorBase64());
        retval.setCipherTransformation(
                getCipherTransformation());
//        getData();
        retval.setEncryptedData(
                getEncryptedData());
        retval.setId(
                getId());
        retval.setSecretKey(
                getSecretKey().toUserSecretKey());
        retval.setVersion(
                getVersion());
        return retval;
    }
}
