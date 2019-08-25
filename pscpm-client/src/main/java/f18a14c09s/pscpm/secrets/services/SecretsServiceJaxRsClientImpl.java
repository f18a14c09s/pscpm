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
package f18a14c09s.pscpm.secrets.services;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import f18a14c09s.pscpm.general.services.IOUtil;
import f18a14c09s.pscpm.secrets.data.CertificateSecret;
import f18a14c09s.pscpm.secrets.data.Secret;
import f18a14c09s.pscpm.secrets.data.ServerSecret;
import f18a14c09s.pscpm.security.data.javacrypto.UserCredential;
import f18a14c09s.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;
import static f18a14c09s.pscpm.general.services.IOUtil.serialize;
import f18a14c09s.pscpm.general.services.ServiceFacade;
import f18a14c09s.pscpm.secrets.data.Certificate;
import f18a14c09s.pscpm.secrets.data.Server;
import f18a14c09s.pscpm.security.data.PublicKeyEncryptedSecretKey;
import f18a14c09s.pscpm.security.services.ISecurityService;
import f18a14c09s.pscpm.security.services.SecurityServiceJaxRsClientImpl;

/**
 *
 * @author fjohnson
 */
public class SecretsServiceJaxRsClientImpl implements ISecretsService {

    private String _baseUrl;
    private final Logger _log = Logger.getLogger(getClass().getName());
    private KeyStore _keyStore;

    public SecretsServiceJaxRsClientImpl() {
    }

    public SecretsServiceJaxRsClientImpl(KeyStore keyStore) {
        setKeyStore(keyStore);
    }

    public SecretsServiceJaxRsClientImpl(KeyStore keyStore, String serverUrl) {
        setKeyStore(keyStore);
        setBaseUrl(serverUrl + "/secrets");
    }

    private Client getDefaultClient() {
        return ClientBuilder.newBuilder().keyStore(getKeyStore(), "Ignore Password").build();
    }

    private KeyStore getKeyStore() {
        return _keyStore;
    }

    private void setKeyStore(KeyStore keyStore) {
        this._keyStore = keyStore;
    }

    private String getBaseUrl() {
        return _baseUrl;
    }

    private void setBaseUrl(String baseUrl) {
        this._baseUrl = baseUrl;
    }

    @Override
    public <EntityClass> Secret<EntityClass> persistSecret(Secret<EntityClass> secret) {
        if (secret != null) {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            try {
                formData.add("secret_to_persist", Base64.getEncoder().encodeToString(serialize(secret)));
            } catch (IOException e) {
                _log.log(Level.SEVERE, "Unable to set request parameters.", e);
            }
            byte[] retvalAB = getDefaultClient().target(getBaseUrl() + "/persist_secret")
                    .request()
                    .post(Entity.form(formData), byte[].class);
            if (retvalAB != null && retvalAB.length >= 1) {
                try {
                    return IOUtil.deserialize(retvalAB);
                } catch (IOException | ClassNotFoundException e) {
                    _log.log(Level.SEVERE, "Unable to deserialize returned value.", e);
                }
            }
        }
        return null;
    }

    @Override
    public <EntityClass> Secret<EntityClass> mergeSecret(Secret<EntityClass> secret) {
        if (secret != null) {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            try {
                formData.add("secret_to_merge", Base64.getEncoder().encodeToString(serialize(secret)));
            } catch (IOException e) {
                _log.log(Level.SEVERE, "Unable to set request parameters.", e);
            }
            byte[] retvalAB = getDefaultClient().target(getBaseUrl() + "/merge_secret")
                    .request()
                    .post(Entity.form(formData), byte[].class);
            if (retvalAB != null && retvalAB.length >= 1) {
                try {
                    return IOUtil.deserialize(retvalAB);
                } catch (IOException | ClassNotFoundException e) {
                    _log.log(Level.SEVERE, "Unable to deserialize returned value.", e);
                }
            }
        }
        return null;
    }

    @Override
    public <EntityClass> Secret<EntityClass> refresh(Secret<EntityClass> secret) {
        if (secret != null) {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            try {
                formData.add("secret_to_refresh", Base64.getEncoder().encodeToString(serialize(secret)));
            } catch (IOException e) {
                _log.log(Level.SEVERE, "Unable to set request parameters.", e);
            }
            byte[] retvalAB = getDefaultClient().target(getBaseUrl() + "/refresh_secret")
                    .request()
                    .post(Entity.form(formData), byte[].class);
            if (retvalAB != null && retvalAB.length >= 1) {
                try {
                    return IOUtil.deserialize(retvalAB);
                } catch (IOException | ClassNotFoundException e) {
                    _log.log(Level.SEVERE, "Unable to deserialize returned value.", e);
                }
            }
        }
        return null;
    }

    @Override
    public void removeSecret(Secret<?> secret) {
        if (secret != null) {
            MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
            try {
                formData.add("secret_to_refresh", Base64.getEncoder().encodeToString(serialize(secret)));
            } catch (IOException e) {
                _log.log(Level.SEVERE, "Unable to set request parameters.", e);
            }
            getDefaultClient().target(getBaseUrl() + "/refresh_secret")
                    .request()
                    .post(Entity.form(formData), byte[].class);
        }
    }

    @Override
    public <L extends java.util.List<f18a14c09s.pscpm.secrets.data.ServerSecret> & java.io.Serializable> L findServers() {
        byte[] retvalAB = getDefaultClient().target(getBaseUrl() + "/find_servers")
                .request()
                .get(byte[].class);
        if (retvalAB != null && retvalAB.length >= 1) {
            try {
                return IOUtil.deserialize(retvalAB);
            } catch (IOException | ClassNotFoundException e) {
                _log.log(Level.SEVERE, "Unable to deserialize return value.", e);
            }
        }
        return null;
    }

    @Override
    public <L extends java.util.List<f18a14c09s.pscpm.secrets.data.CertificateSecret> & java.io.Serializable> L findCertificates() {
        byte[] retvalAB = getDefaultClient().target(getBaseUrl() + "/find_certs")
                .request()
                .get(byte[].class);
        if (retvalAB != null && retvalAB.length >= 1) {
            try {
                return IOUtil.deserialize(retvalAB);
            } catch (IOException | ClassNotFoundException e) {
                _log.log(Level.SEVERE, "Unable to deserialize return value.", e);
            }
        }
        return null;
    }

    @Override
    public <L extends java.util.List<f18a14c09s.pscpm.secrets.data.ServerSecret> & java.io.Serializable> L findAvailableServerSecrets() {
        byte[] retvalAB = getDefaultClient().target(getBaseUrl() + "/find_avail_server_secrets")
                .request()
                .get(byte[].class);
        if (retvalAB != null && retvalAB.length >= 1) {
            try {
                return IOUtil.deserialize(retvalAB);
            } catch (IOException | ClassNotFoundException e) {
                _log.log(Level.SEVERE, "Unable to deserialize return value.", e);
            }
        }
        return null;
    }

    @Override
    public ServerSecret findServer(long id) {
        byte[] retvalAB = getDefaultClient().target(getBaseUrl() + String.format("/find_server/%s", id))
                .request()
                .get(byte[].class);
        if (retvalAB != null && retvalAB.length >= 1) {
            try {
                return IOUtil.deserialize(retvalAB);
            } catch (IOException | ClassNotFoundException e) {
                _log.log(Level.SEVERE, "Unable to deserialize return value.", e);
            }
        }
        return null;
    }

    @Override
    public CertificateSecret findCertificate(long id) {
        byte[] retvalAB = getDefaultClient().target(getBaseUrl() + String.format("/find_cert/%s", id))
                .request()
                .get(byte[].class);
        if (retvalAB != null && retvalAB.length >= 1) {
            try {
                return IOUtil.deserialize(retvalAB);
            } catch (IOException | ClassNotFoundException e) {
                _log.log(Level.SEVERE, "Unable to deserialize return value.", e);
            }
        }
        return null;
    }

    public static void main(String... args) {
        Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(2017, 1, 1);
            UserCredential cred = IdentityKeyStoreAdapter.getBasicEncryptionCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
            ISecretsService svc = new SecretsServiceJaxRsClientImpl(cred.getKeyStore());
            ISecurityService sec = new SecurityServiceJaxRsClientImpl(cred.getKeyStore(), ServiceFacade.DEFAULT_SERVER_URL);
            PublicKeyEncryptedSecretKey secretKey = sec.findUserSecretKeys().get(0);
            Secret<Server> server = new ServerSecret(new Server(), secretKey.getSecretKey());
            server.getData().setDescription("Unit test server secret.");
            server.getData().setEnvironment("Development");
            server.getData().setIpAddress("1.2.3.4");
            server.getData().setName("unit-tst-svr-" + System.currentTimeMillis());
            server.getData().setOsVersion("Unix");
            server = svc.persistSecret(server);
            log.info("persistSecret(): " + server + ".");
            server = svc.findServer(server.getId());
            log.info("findServer(): " + server + ".");
            server = svc.mergeSecret(server);
            log.info("mergeSecret(): " + server + ".");
            server = svc.refresh(server);
            log.info("refresh(): " + server + ".");
            List<ServerSecret> servers = svc.findAvailableServerSecrets();
            log.info("findAvailableServerSecrets(): " + servers + ".");
            svc.removeSecret(server);
            Secret<Certificate> cert = new CertificateSecret();
            cert.setData(cert.getData() == null ? new Certificate() : cert.getData());
            cert.getData().setAliasPassword("abcdef".getBytes(StandardCharsets.UTF_8));
            cert.getData().setCertAuthorityId("nunyo");
            cert.getData().setEnvironment("Test");
            cert.getData().setExpirationDate(new Date());
            cert.getData().setLocation("nunyo");
            cert.getData().setName("Unit test certificate secret.");
            cert.getData().setRequestNumber("REQ-12345");
            cert.getData().setStoreName("hmm");
            cert.getData().setStorePassword("nunyo".getBytes(StandardCharsets.UTF_8));
            cert.getData().setStoreType(Certificate.StoreType.JKS);
            cert = svc.persistSecret(cert);
            log.info("persistSecret(): " + cert + ".");
            cert = svc.findCertificate(cert.getId());
            log.info("findCertificate(): " + cert + ".");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
