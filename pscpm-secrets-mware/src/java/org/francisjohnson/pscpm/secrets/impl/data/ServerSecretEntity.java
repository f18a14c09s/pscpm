package org.francisjohnson.pscpm.secrets.impl.data;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.francisjohnson.pscpm.secrets.data.Secret;
import org.francisjohnson.pscpm.secrets.data.Server;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;

import org.francisjohnson.pscpm.security.impl.data.UserSecretKeyEntity;

@Entity
@NamedQueries({
    @NamedQuery(name = "ServerSecretEntity.findAll",
            query = "select o from ServerSecretEntity o")
    ,@NamedQuery(name = "ServerSecretEntity.findByUser", query = "select o from ServerSecretEntity o, PublicKeyEncryptedSecretKeyEntity k"
            + " where o.secretKey = k.secretKey"
            + " and k.owner = :user")})
@Inheritance
@DiscriminatorValue("SERVER")
public class ServerSecretEntity extends SecretEntity<Server> implements Serializable {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;
    
    public ServerSecretEntity() {
        setData(new Server());
    }
    
    public ServerSecretEntity(byte[] encryptedData, UserSecretKeyEntity secretKey,
            byte[] initVector, String cipherAlgorithm) {
        super(encryptedData, secretKey, initVector, cipherAlgorithm);
        if (encryptedData == null || encryptedData.length <= 0) {
            setData(new Server());
        }
    }
    
    public ServerSecretEntity(Server data, UserSecretKeyEntity secretKey) {
        setData(data);
        setSecretKey(secretKey);
    }
    
    public ServerSecretEntity(Secret<Server> secret) {
        super(secret);
    }
    
    public Server getServer() {
        return super.getData();
    }
    
    @Override
    public ServerSecret toSecret() {
        ServerSecret retval = new ServerSecret();
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
