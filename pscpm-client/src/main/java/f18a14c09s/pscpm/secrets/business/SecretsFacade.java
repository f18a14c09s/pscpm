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
package f18a14c09s.pscpm.secrets.business;

import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import f18a14c09s.pscpm.general.data.MapOfLists;
import f18a14c09s.pscpm.general.services.ServiceFacade;
import f18a14c09s.pscpm.secrets.services.FileAndObjectIOAdapter;
import f18a14c09s.pscpm.secrets.services.ISecretsService;
import f18a14c09s.pscpm.security.business.SecurityFacade;
import f18a14c09s.pscpm.security.data.SecurityException;
import f18a14c09s.pscpm.security.data.javacrypto.UserCredential;
import f18a14c09s.pscpm.security.services.PasswordGenerator;
import f18a14c09s.pscpm.security.services.javacrypto.DataDecryptionAdapter;
import f18a14c09s.pscpm.security.services.javacrypto.DataEncryptionAdapter;
import f18a14c09s.pscpm.secrets.data.Certificate;
import f18a14c09s.pscpm.secrets.data.CertificateSecret;
import f18a14c09s.pscpm.secrets.data.Secret;
import f18a14c09s.pscpm.secrets.data.SecretsException;
import f18a14c09s.pscpm.secrets.data.ServerSecret;
import f18a14c09s.pscpm.security.data.OfflineUser;
import f18a14c09s.pscpm.security.data.PublicKeyEncryptedKey;
import f18a14c09s.pscpm.security.data.PublicKeyEncryptedSecretKey;
import f18a14c09s.pscpm.security.data.User;
import f18a14c09s.pscpm.security.data.UserSecretKey;

public class SecretsFacade {

    private Logger logger
            = Logger.getLogger(this.getClass().getPackage().getName());
    private SecureRandom rand = new SecureRandom();
    private String remoteUrl;
    private UserCredential credential;
    private SecurityFacade security = new SecurityFacade();
    private User user = new OfflineUser();
    private ISecretsService secrets;

    public SecretsFacade(UserCredential credential) throws SecretsException {
        super();
        setCredential(credential);
        try {
            setUser(new User(null, credential.getCert()));
        } catch (Exception e) {
            throw new SecretsException("Constructor failed while creating non-persistent user object.",
                    e);
        }
        setSecurity(new SecurityFacade());
    }

    private SecretsFacade(String remoteUrl,
            UserCredential credential) throws SecretsException {
        super();
        setRemoteUrl(remoteUrl);
        setCredential(credential);
        try {
            setSecrets((ISecretsService) ServiceFacade.<ISecretsService>getInstance(ISecretsService.class,
                    remoteUrl,
                    credential).newFacade());
            setSecurity(new SecurityFacade(remoteUrl, credential));
        } catch (Exception e) {
            throw new SecretsException("Unable to contact secrets service.",
                    e);
        }
    }

    // TODO: Decide whether it's a problem to assume that a null user is "offline."
    public boolean isOffline() {
        return getUser() instanceof OfflineUser || getUser() == null;
    }

    public static SecretsFacade startSession(String remoteUrl,
            UserCredential credential) throws SecretsException {
        SecretsFacade retval = new SecretsFacade(remoteUrl, credential);
        retval.login();
        return retval;
    }

    private void login() throws SecretsException {
        try {
            User user = getSecurity().login();
            if (user == null) {
                throw new SecretsException("Login failed.  Reason unknown.");
            } else {
                setUser(user);
            }
        } catch (SecurityException e) {
            throw new SecretsException("Login failed.", e);
        }
    }

    //    /**
    //     * In the CRUD, ABCD, and BREAD lifecycles, this corresponds to delete.
    //     * @param server
    //     * @throws SecretsException
    //     */
    //    public void delete(Server server) throws SecretsException {
    //        try {
    //            if (isOffline()) {
    //                // TODO: Replace but indicate with caveats.
    //                throw new SecretsException("Currently offline.  Deletion not available.");
    //            } else {
    //                ServerSecret secret = getSecrets().findServer(server.getId());
    //                getSecrets().removeSecret(secret);
    //            }
    //        } catch (Exception e) {
    //            throw new SecretsException("Unable to add server secret.", e);
    //        } finally {
    //        }
    //    }
    /**
     * In the CRUD, ABCD, and BREAD lifecycles, this corresponds to delete.
     *
     * @param secret
     * @throws SecretsException
     */
    public void delete(Secret<?> secret) throws SecretsException {
        try {
            if (isOffline()) {
                FileAndObjectIOAdapter.delete(secret);
                throw new SecretsException("Currently offline.  Local cache updated, but online Delete not available.");
            } else {
                getSecrets().removeSecret(secret);
                FileAndObjectIOAdapter.delete(secret);
            }
        } catch (Exception e) {
            throw new SecretsException("Unable to add server secret.", e);
        } finally {
        }
    }

    //    /**
    //     * In the CRUD, ABCD, and BREAD lifecycles, this corresponds to update,
    //     * change, and edit, respectively.
    //     * @param server
    //     * @param secretKey
    //     * @return
    //     * @throws SecretsException
    //     */
    //    public ServerSecret save(Server server,
    //                             PublicKeyEncryptedSecretKey secretKey) throws SecretsException {
    //        try {
    //            ServerSecret managedRecord =
    //                getSecrets().findServer(server.getId());
    //            DataEncryptionPrototype.CipherData encryptedData =
    //                DataEncryptionPrototype.encrypt(server, secretKey);
    //            managedRecord.setEncryptedData(encryptedData.getData(),
    //                                           secretKey.getSecretKey(),
    //                                           encryptedData.getInitVector(),
    //                                           encryptedData.getTransformation());
    //            if (isOffline()) {
    //                LocalCachePrototype.cache(managedRecord);
    //                throw new SecretsException("Currently offline.  Local cache updated, but online Save not available.");
    //            } else {
    //                return (ServerSecret)secrets.mergeSecret(managedRecord);
    //            }
    //        } catch (Exception e) {
    //            throw new SecretsException("Unable to add server secret.", e);
    //        } finally {
    //        }
    //    }
    /**
     * In the CRUD, ABCD, and BREAD lifecycles, this corresponds to update,
     * change, and edit, respectively.
     *
     * @param serverSecret
     * @return
     * @throws SecretsException
     */
    public ServerSecret save(ServerSecret serverSecret) throws SecretsException {
        try {
            // TODO: Handle the validation errors directly
            serverSecret.toString();
            serverSecret.getData().toString();
            serverSecret.getSecretKey().toString();
            PublicKeyEncryptedSecretKey encryptedKey
                    = findEncryptedKey(serverSecret.getSecretKey());
            // TODO: Handle the lack of a match directly.
            encryptedKey.toString();
            ServerSecret managedRecord
                    = isOffline() ? (ServerSecret) serverSecret
                            : getSecrets().findServer(serverSecret.getId());
            DataEncryptionAdapter.CipherData encryptedData
                    = DataEncryptionAdapter.encrypt(serverSecret.getData(),
                            encryptedKey);
            managedRecord.setEncryptedData(encryptedData.getData(),
                    encryptedKey.getSecretKey(),
                    encryptedData.getInitVector(),
                    encryptedData.getTransformation());
            if (isOffline()) {
                FileAndObjectIOAdapter.cache(managedRecord);
                throw new SecretsException("Currently offline.  Local cache updated, but online Save not available.");
            } else {
                ServerSecret retval
                        = (ServerSecret) getSecrets().mergeSecret(managedRecord);
                FileAndObjectIOAdapter.cache(managedRecord);
                retval.setData(decrypt(retval));
                return retval;
            }
        } catch (Exception e) {
            throw new SecretsException("Unable to add server secret.", e);
        } finally {
        }
    }

    //    /**
    //     * In the CRUD, ABCD, and BREAD lifecycles, this corresponds to create, add
    //     * and add, respectively.
    //     * @param server
    //     * @param secretKey
    //     * @return
    //     * @throws SecretsException
    //     */
    //    public ServerSecret add(Server server,
    //                            PublicKeyEncryptedSecretKey secretKey) throws SecretsException {
    //        try {
    //            //            byte[][] encryptedData =
    //            //                DataEncryptionPrototype.encrypt(server, secretKey);
    //            //            ServerSecret secret =
    //            //                new ServerSecret(encryptedData[0], secretKey.getSecretKey(),
    //            //                                 encryptedData[1]);
    //            DataEncryptionPrototype.CipherData encryptedData =
    //                DataEncryptionPrototype.encrypt(server, secretKey);
    //            ServerSecret managedRecord =
    //                new ServerSecret(encryptedData.getData(),
    //                                 secretKey.getSecretKey(),
    //                                 encryptedData.getInitVector(),
    //                                 encryptedData.getTransformation());
    //            if (isOffline()) {
    //                managedRecord.setId(managedRecord.getId() == null ?
    //                                    getRand().nextLong() :
    //                                    managedRecord.getId());
    //                LocalCachePrototype.cache(managedRecord);
    //                throw new SecretsException("Currently offline.  Local cache updated, but online Add not available.");
    //            } else {
    //                return (ServerSecret)getSecrets().persistSecret(managedRecord);
    //            }
    //        } catch (Exception e) {
    //            throw new SecretsException("Unable to add server secret.", e);
    //        } finally {
    //        }
    //    }
    private PublicKeyEncryptedSecretKey findEncryptedKey(UserSecretKey secretKey) throws SecurityException {
        for (PublicKeyEncryptedSecretKey tempKey
                : getSecurity().findMySecretKeys()) {
            if (tempKey.getSecretKey().getId().equals(secretKey.getId())) {
                return tempKey;
            }
        }
        return null;
    }

    /**
     * In the CRUD, ABCD, and BREAD lifecycles, this corresponds to create, add
     * and add, respectively.
     *
     * @param serverSecret
     * @return
     * @throws SecretsException
     */
    public ServerSecret add(ServerSecret serverSecret) throws SecretsException {
        try {
            // TODO: Handle the validation errors directly
            serverSecret.toString();
            serverSecret.getData().toString();
            serverSecret.getSecretKey().toString();
            PublicKeyEncryptedSecretKey encryptedKey
                    = findEncryptedKey(serverSecret.getSecretKey());
            // TODO: Handle the lack of a match directly.
            encryptedKey.toString();
            //            byte[][] encryptedData =
            //                DataEncryptionPrototype.encrypt(server, secretKey);
            //            ServerSecret secret =
            //                new ServerSecret(encryptedData[0], secretKey.getSecretKey(),
            //                                 encryptedData[1]);
            DataEncryptionAdapter.CipherData encryptedData
                    = DataEncryptionAdapter.encrypt(serverSecret.getData(),
                            encryptedKey);
            ServerSecret managedRecord
                    = new ServerSecret(encryptedData.getData(),
                            encryptedKey.getSecretKey(),
                            encryptedData.getInitVector(),
                            encryptedData.getTransformation());
            if (isOffline()) {
                managedRecord.setId(managedRecord.getId() == null
                        ? getRand().nextLong()
                        : managedRecord.getId());
                FileAndObjectIOAdapter.cache(managedRecord);
                throw new SecretsException("Currently offline.  Local cache updated, but online Add not available.");
            } else {
                ServerSecret retval
                        = (ServerSecret) getSecrets().persistSecret(managedRecord);
                FileAndObjectIOAdapter.cache(retval);
                retval.setData(decrypt(retval));
                return retval;
            }
        } catch (SecretsException e) {
            throw e;
        } catch (Exception e) {
            throw new SecretsException("Unable to add server secret.", e);
        } finally {
        }
    }

    /**
     * In the CRUD, ABCD, and BREAD lifecycles, this corresponds to create, add
     * and add, respectively.
     *
     * @param cert
     * @param secretKey
     * @return
     * @throws SecretsException
     */
    public Secret<Certificate> add(Certificate cert,
                                   PublicKeyEncryptedSecretKey secretKey) throws SecretsException {
        try {
            DataEncryptionAdapter.CipherData encryptedData
                    = DataEncryptionAdapter.encrypt(cert, secretKey);
            CertificateSecret secret
                    = new CertificateSecret(encryptedData.getData(),
                            secretKey.getSecretKey(),
                            encryptedData.getInitVector(),
                            encryptedData.getTransformation());
            if (isOffline()) {
                secret.setId(secret.getId() == null ? getRand().nextLong()
                        : secret.getId());
                FileAndObjectIOAdapter.cache(secret);
                throw new SecretsException("Currently offline.  Local cache updated, but online Add not available.");
            } else {
                //            byte[][] encryptedData =
                //                DataEncryptionPrototype.encrypt(cert, secretKey);
                //            CertificateSecret secret =
                //                new CertificateSecret(encryptedData[0], secretKey.getSecretKey(),
                //                                      encryptedData[1]);
                Secret<Certificate> retval
                        = getSecrets().persistSecret(secret);
                FileAndObjectIOAdapter.cache(retval);
                return retval;
            }
        } catch (Exception e) {
            throw new SecretsException("Unable to add certificate secret.", e);
        } finally {
        }
    }

    /**
     * In the CRUD and BREAD lifecycles, this corresponds to read.
     *
     * @param secret
     * @return
     * @throws SecretsException
     */
    public <EntityClass> EntityClass decrypt(Secret<EntityClass> secret) throws SecretsException {
        try {
            PublicKeyEncryptedSecretKey selectedKey = null;
            for (PublicKeyEncryptedSecretKey key
                    : getSecurity().findMySecretKeys()) {
                if (key.getSecretKey().getId().equals(secret.getSecretKey().getId())) {
                    selectedKey = key;
                    break;
                }
            }
            // TODO: Replace with null validation.
            selectedKey.toString();
            return (EntityClass) DataDecryptionAdapter.decrypt(secret,
                    selectedKey);
        } catch (Exception e) {
            throw new SecretsException("Unable to decrypt secret.", e);
        } finally {
        }
    }

    /**
     * In the CRUD and BREAD lifecycles, this corresponds to read.
     *
     * @param secrets
     * @return
     * @throws SecretsException
     */
    public List<Secret<?>> decrypt(List<Secret<?>> secrets) throws SecretsException {
        try {
            Map<Number, PublicKeyEncryptedSecretKey> keysById
                    = new HashMap<Number, PublicKeyEncryptedSecretKey>();
            for (PublicKeyEncryptedSecretKey key
                    : getSecurity().findMySecretKeys()) {
                keysById.put(key.getSecretKey().getId(), key);
            }
            MapOfLists<Number, Secret<?>> secretsByKeyId
                    = new MapOfLists<Number, Secret<?>>();
            for (Secret<?> secret : secrets) {
                secretsByKeyId.addToList(secret.getSecretKey().getId(),
                        secret);
            }
            for (Map.Entry<Number, List<Secret<?>>> subList
                    : secretsByKeyId.entrySet()) {
                // TODO: Replace with null validation.
                keysById.get(subList.getKey()).toString();
                DataDecryptionAdapter.decrypt((List<Secret<Object>>) (Object) subList.getValue(),
                        keysById.get(subList.getKey()));
            }
            return secrets;
        } catch (Exception e) {
            throw new SecretsException("Unable to decrypt secret.", e);
        } finally {
        }
    }

    /**
     * In the CRUD and BREAD lifecycles, this corresponds to read.
     *
     * @param secret
     * @return
     * @throws SecretsException
     */
    public <EntityClass> Secret<EntityClass> refresh(Secret<EntityClass> secret) throws SecretsException {
        try {
            if (isOffline()) {
                // TODO: Replace but indicate with caveats.
                throw new SecretsException("Currently offline.  Refresh not available.");
            } else {
                return getSecrets().refresh(secret);
            }
        } catch (Exception e) {
            throw new SecretsException("Unable to refresh secret.", e);
        } finally {
        }
    }

    /**
     * In the ABCD and BREAD lifecycles, this corresponds to browse.
     *
     * @return
     * @throws SecretsException
     */
    public List<ServerSecret> findAvailableServers() throws SecretsException {
        try {
            List<ServerSecret> retval = null;
            if (isOffline()) {
                retval
                        = (List<ServerSecret>) (Object) FileAndObjectIOAdapter.loadAllByInterface(ServerSecret.class);
            } else {
                retval = getSecrets().findAvailableServerSecrets();
                FileAndObjectIOAdapter.cache(retval);
            }
            decrypt((List<Secret<?>>) (Object) retval);
            return retval;
        } catch (Exception e) {
            throw new SecretsException("Unable to find available server secrets.",
                    e);
        } finally {
        }
    }

    private void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    private String getRemoteUrl() {
        return remoteUrl;
    }

    private void setCredential(UserCredential credential) {
        this.credential = credential;
    }

    private UserCredential getCredential() {
        return credential;
    }

    private void setSecurity(SecurityFacade security) {
        this.security = security;
    }

    private SecurityFacade getSecurity() {
        return security;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    private void setSecrets(ISecretsService secrets) {
        this.secrets = secrets;
    }

    private ISecretsService getSecrets() {
        return secrets;
    }

    /**
     * @deprecated Because the system supports sharing and one cannot share a
     * public-key-encrypted password without sharing the private key.
     * @param password
     * @return
     * @throws SecurityException
     */
    @Deprecated
    public PublicKeyEncryptedKey encryptAndDestroy(char[] password) throws SecretsException {
        try {
            return getSecurity().encryptAndDestroy(password,
                    user.getX509Certificate());
        } catch (Exception e) {
            throw new SecretsException("Unable to encrypt the password.", e);
        } finally {
        }
    }

    /**
     * @deprecated Because the system supports sharing and one cannot share a
     * public-key-encrypted password without sharing the private key.
     * @param encryptedPassword
     * @return
     * @throws SecurityException
     */
    @Deprecated
    public char[] decryptPassword(PublicKeyEncryptedKey encryptedPassword) throws SecretsException {
        try {
            return getSecurity().decryptPassword(encryptedPassword);
        } catch (Exception e) {
            throw new SecretsException("Unable to decrypt the encrypted password.",
                    e);
        } finally {
        }
    }

    public char[] generatePassword() {
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword();
    }

    public List<UserSecretKey> findMySecretKeys() throws SecretsException {
        List<UserSecretKey> retval = new ArrayList<UserSecretKey>();
        try {
            for (PublicKeyEncryptedSecretKey key
                    : getSecurity().findMySecretKeys()) {
                retval.add(key.getSecretKey());
            }
        } catch (Exception e) {
            throw new SecretsException("Unable to find user's secret keys.",
                    e);
        } finally {
        }
        return retval;
    }

    private void setLogger(Logger logger) {
        this.logger = logger;
    }

    private Logger getLogger() {
        return logger;
    }

    private void setRand(SecureRandom rand) {
        this.rand = rand;
    }

    private SecureRandom getRand() {
        return rand;
    }

    public void endSession() {
        getSecurity().logout();
        setCredential(null);
        setRemoteUrl(null);
        setSecrets(null);
        setSecurity(null);
        setUser(new OfflineUser());
    }
}
