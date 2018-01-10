package org.francisjohnson.pscpm.secrets.services;

import java.io.Serializable;
import java.util.List;

import org.francisjohnson.pscpm.secrets.data.CertificateSecret;
import org.francisjohnson.pscpm.secrets.data.Secret;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.general.services.IService;

public interface ISecretsService extends IService {

    <EntityClass> Secret<EntityClass> persistSecret(Secret<EntityClass> secret);

    <EntityClass> Secret<EntityClass> mergeSecret(Secret<EntityClass> secret);

    void removeSecret(Secret<?> secret);

    <L extends List<ServerSecret> & Serializable> L findServers();

    <L extends List<CertificateSecret> & Serializable> L findCertificates();

    <EntityClass> Secret<EntityClass> refresh(Secret<EntityClass> secret);

    <L extends List<ServerSecret> & Serializable> L findAvailableServerSecrets();

    ServerSecret findServer(long id);

    CertificateSecret findCertificate(long id);
}
