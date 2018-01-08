package org.francisjohnson.pscpm.secrets.data;

import java.io.Serializable;

import org.francisjohnson.pscpm.security.data.UserSecretKey;


public class ServerSecret extends Secret<Server> implements Serializable {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public ServerSecret() {
        setData(new Server());
    }

    public ServerSecret(byte[] encryptedData, UserSecretKey secretKey,
                        byte[] initVector, String cipherAlgorithm) {
        super(encryptedData, secretKey, initVector, cipherAlgorithm);
        if (encryptedData == null || encryptedData.length <= 0) {
            setData(new Server());
        }
    }

    public ServerSecret(Server data, UserSecretKey secretKey) {
        setData(data);
        setSecretKey(secretKey);
    }

    public Server getServer() {
        return super.getData();
    }
}
