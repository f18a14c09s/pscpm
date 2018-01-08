package org.francisjohnson.pscpm.secrets.services;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.francisjohnson.pscpm.secrets.data.CertificateSecret;
import org.francisjohnson.pscpm.secrets.data.Secret;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;

import org.francisjohnson.pscpm.secrets.impl.data.CertificateSecretEntity;
import org.francisjohnson.pscpm.secrets.impl.data.SecretEntity;
import org.francisjohnson.pscpm.secrets.impl.data.ServerSecretEntity;
import org.francisjohnson.pscpm.security.services.SecuritySvcRemoteFacade;

/**
 * This facade is remote in the sense that it is accessed remotely by the client
 * tier. The client tier's alternative is to use a cache DAO which serializes
 * objects to the local file system. The terms "remote" and "local" in this
 * sense are not analogous to EJB's traditional remote and local
 * homes/interfaces.
 */
@Stateless(name = "SecretsSvcRemoteFacade",
        mappedName = "pscpm-secrets-ejb")
public class SecretsSvcRemoteFacadeBean implements ISecretsService,
        SecretsSvcRemoteFacade {

    @PersistenceContext(unitName = "pscpm-secrets-jpa")
    private EntityManager em;

    @EJB
    private SecuritySvcRemoteFacade security;

    public SecretsSvcRemoteFacadeBean() {
    }

    private Object queryByRange(String jpqlStmt, int firstResult,
            int maxResults) {
        Query query = em.createQuery(jpqlStmt);
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    @Override
    public <EntityClass> Secret<EntityClass> persistSecret(Secret<EntityClass> secret) {
        em.persist(secret);
        return secret;
    }

    @Override
    public <EntityClass> Secret<EntityClass> mergeSecret(Secret<EntityClass> secret) {
        return em.merge(secret);
    }

    @Override
    public void removeSecret(Secret<?> secret) {
        SecretEntity entity = em.find(SecretEntity.class, secret.getId());
        em.remove(entity);
    }

    @Override
    public List<ServerSecret> findServers() {
        // TODO: Limit the results to the current user.
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CertificateSecret> findCertificates() {
        // TODO: Limit the results to the current user.
        throw new UnsupportedOperationException();
    }

    @Override
    public <EntityClass> Secret<EntityClass> refresh(Secret<EntityClass> secret) {
        SecretEntity entity
                = em.find(SecretEntity.class, secret.getId());
        return entity == null ? null : entity.toSecret();
    }

    @Override
    public List<ServerSecret> findAvailableServerSecrets() {
        return em.createNamedQuery("ServerSecretEntity.findByUser").setParameter("user",
                security.getCurrentUser()).getResultList();
    }

    @Override
    public ServerSecret findServer(long id) {
        try {
            ServerSecretEntity entity = em.find(ServerSecretEntity.class, id);
            return entity == null ? null : entity.toSecret();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public CertificateSecret findCertificate(long id) {
        try {
            CertificateSecretEntity entity = em.find(CertificateSecretEntity.class, id);
            return entity == null ? null : entity.toSecret();
        } catch (NoResultException e) {
            return null;
        }
    }
}
