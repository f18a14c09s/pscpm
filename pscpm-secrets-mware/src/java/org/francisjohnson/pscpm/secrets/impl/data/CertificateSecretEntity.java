package org.francisjohnson.pscpm.secrets.impl.data;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.francisjohnson.pscpm.secrets.data.Certificate;
import org.francisjohnson.pscpm.secrets.data.CertificateSecret;

import org.francisjohnson.pscpm.security.impl.data.UserSecretKeyEntity;

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
