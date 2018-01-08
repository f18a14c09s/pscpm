package org.francisjohnson.pscpm.secrets.services;

import java.util.List;

import org.francisjohnson.pscpm.secrets.data.CertificateSecret;
import org.francisjohnson.pscpm.secrets.data.Secret;
import org.francisjohnson.pscpm.secrets.data.Server;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.general.services.IService;


public interface ISecretsService extends IService {
    <EntityClass> Secret<EntityClass> persistSecret(Secret<EntityClass> secret);

    <EntityClass> Secret<EntityClass> mergeSecret(Secret<EntityClass> secret);

    void removeSecret(Secret<?> secret);

    List<ServerSecret> findServers();

    List<CertificateSecret> findCertificates();

    <EntityClass> Secret<EntityClass> refresh(Secret<EntityClass> secret);

    List<ServerSecret> findAvailableServerSecrets();

    ServerSecret findServer(long id);

    CertificateSecret findCertificate(long id);
}
