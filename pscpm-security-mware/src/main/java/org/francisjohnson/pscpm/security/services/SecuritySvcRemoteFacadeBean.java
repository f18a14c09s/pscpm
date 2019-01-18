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
package org.francisjohnson.pscpm.security.services;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.francisjohnson.pscpm.security.impl.data.AdministratorEntity;
import org.francisjohnson.pscpm.security.impl.data.LoginAttemptEntity;
import org.francisjohnson.pscpm.security.impl.data.PublicKeyEncryptedSecretKeyEntity;
import org.francisjohnson.pscpm.security.impl.data.SecurityPrincipalEntity;
import org.francisjohnson.pscpm.security.impl.data.UserEntity;
import org.francisjohnson.pscpm.security.impl.data.UserSecretKeyEntity;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.SecurityPrincipal;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.services.javacrypto.KeyGeneratorWrapper;
import org.francisjohnson.pscpm.security.services.javacrypto.KeyEncryptionAdapter;

/**
 * This facade is remote in the sense that it is accessed remotely by the client
 * tier. The client tier's alternative is to use a cache DAO which serializes
 * objects to the local file system. The terms "remote" and "local" in this
 * sense are not analogous to EJB's traditional remote and local
 * homes/interfaces.
 */
@Stateless(name = "SecuritySvcRemoteFacade",
        mappedName = "pscpm-security-ejb")
public class SecuritySvcRemoteFacadeBean implements ISecurityService,
        SecuritySvcRemoteFacade {

    @PersistenceContext(unitName = "pscpm-security-jpa")
    private EntityManager em;

    @Resource
    private SessionContext sess;

    private final Logger _log = Logger.getLogger(getClass().getName());

    public SecuritySvcRemoteFacadeBean() {
    }

    private Logger getLog() {
        return _log;
    }

    public static ISecurityService getInstance() {
        try {
            return (ISecurityService) new InitialContext().lookup(SecuritySvcRemoteFacadeBean.class.getAnnotation(Stateless.class).mappedName() + "#" + SecuritySvcRemoteFacade.class.getName());
        } catch (NamingException e) {
            return null;
        }
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

    private UserEntity persistUser(UserEntity user) {
        em.persist(user);
        return user;
    }

    private void removeUser(UserEntity user) {
        user = em.find(UserEntity.class, user.getId());
        em.remove(user);
    }

    private UserSecretKeyEntity persistUserSecretKey(UserSecretKeyEntity userSecretKey) {
        em.persist(userSecretKey);
        return userSecretKey;
    }

    private UserSecretKeyEntity mergeUserSecretKey(UserSecretKeyEntity userSecretKey) {
        return em.merge(userSecretKey);
    }

    private void removeUserSecretKey(UserSecretKeyEntity userSecretKey) {
        userSecretKey = em.find(UserSecretKeyEntity.class, userSecretKey.getId());
        em.remove(userSecretKey);
    }

    private SecurityPrincipalEntity persistPrincipal(SecurityPrincipalEntity principal) {
        em.persist(principal);
        return principal;
    }

    private SecurityPrincipalEntity mergePrincipal(SecurityPrincipalEntity principal) {
        return em.merge(principal);
    }

    private void removePrincipal(SecurityPrincipalEntity principal) {
        principal = em.find(SecurityPrincipalEntity.class, principal.getId());
        em.remove(principal);
    }

    private AdministratorEntity persistAdministrator(AdministratorEntity administrator) {
        em.persist(administrator);
        return administrator;
    }

    private AdministratorEntity mergeAdministrator(AdministratorEntity administrator) {
        return em.merge(administrator);
    }

    private void removeAdministrator(AdministratorEntity administrator) {
        administrator = em.find(AdministratorEntity.class, administrator.getId());
        em.remove(administrator);
    }

    private LoginAttemptEntity persistLoginAttempt(LoginAttemptEntity loginAttempt) {
        em.persist(loginAttempt);
        return loginAttempt;
    }

    private LoginAttemptEntity mergeLoginAttempt(LoginAttemptEntity loginAttempt) {
        return em.merge(loginAttempt);
    }

    private void removeLoginAttempt(LoginAttemptEntity loginAttempt) {
        loginAttempt = em.find(LoginAttemptEntity.class, loginAttempt.getId());
        em.remove(loginAttempt);
    }

    private PublicKeyEncryptedSecretKeyEntity persistPublicKeyEncryptedSecretKey(PublicKeyEncryptedSecretKeyEntity publicKeyEncryptedSecretKey) {
        em.persist(publicKeyEncryptedSecretKey);
        return publicKeyEncryptedSecretKey;
    }

    private PublicKeyEncryptedSecretKeyEntity mergePublicKeyEncryptedSecretKey(PublicKeyEncryptedSecretKeyEntity publicKeyEncryptedSecretKey) {
        return em.merge(publicKeyEncryptedSecretKey);
    }

    private void removePublicKeyEncryptedSecretKey(PublicKeyEncryptedSecretKeyEntity publicKeyEncryptedSecretKey) {
        publicKeyEncryptedSecretKey
                = em.find(PublicKeyEncryptedSecretKeyEntity.class,
                        publicKeyEncryptedSecretKey.getId());
        em.remove(publicKeyEncryptedSecretKey);
    }

    private SecurityPrincipalEntity findPrincipal(String userId) throws CertificateEncodingException,
            NoSuchAlgorithmException {
        try {
            return userId == null ? null
                    : (SecurityPrincipalEntity) em.createNamedQuery("SecurityPrincipalEntity.findByUserId").setParameter("userid",
                            userId).getSingleResult();
        } catch (NoResultException e) {
            /* This exception is normal behavior which is logged elsewhere. */
            return null;
        }
    }

    public UserEntity getCurrentUserImpl() {
        try {
            return (UserEntity) em.createNamedQuery("UserEntity.findByUserId").setParameter("userid",
                    sess.getCallerPrincipal().getName()).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User mergeUser(User user) {
        if (user != null && getCurrentUser() != null
                && user.getId().equals(getCurrentUser().getId())) {
            UserEntity entity = new UserEntity(user);
            return em.merge(entity).toUser();
        }
        // TODO: Throw an exception.
        getLog().warning(String.format("Cannot update other users' profiles: Requestor=%s, User=%s.", getCurrentUser() == null ? "Unknown" : getCurrentUser().getUserId(),
                user == null ? "Not Specified" : user.getUserId()
        ));
        return null;
    }

    @Override
    public SecurityPrincipal findPrincipal(X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException {
        SecurityPrincipalEntity principal = null;
        if (cert != null) {
            getLog().fine("Principal: "
                    + cert.getSubjectX500Principal().getName());
            principal = new SecurityPrincipalEntity(null, cert);
            try {
                SecurityPrincipalEntity retvalSPE = (SecurityPrincipalEntity) em.createNamedQuery("SecurityPrincipalEntity.findByPubKeyFprint").setParameter("fingerprint",
                        principal.getCachedPubKeyFprintBase64()).getSingleResult();
                return retvalSPE == null ? null : retvalSPE.toSecurityPrincipal();
            } catch (NoResultException e) {
                /* This exception is normal behavior which is logged elsewhere. */
            }
        }
        return null;
    }

    @Override
    public User authenticate(X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException,
            NonUniqueResultException {
//        getLog().fine(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "Certificate " + (cert == null ? "is null" : "is non-null")));
        UserEntity retval = null;
        if (cert != null) {
            SecurityPrincipalEntity principal = null;
            String userId = sess.getCallerPrincipal().getName();
            principal
                    = new SecurityPrincipalEntity(sess.getCallerPrincipal().getName(),
                            cert);
            SecurityPrincipalEntity match = findPrincipal(userId);
//            getLog().fine(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "Security Principal Found?: " + match + "."));
            if (match == null) {
                persistPrincipal(principal);
            } else if (match instanceof UserEntity) {
                retval = (UserEntity) match;
            } else {
                principal = match;
            }
            LoginAttemptEntity securityAudit
                    = retval == null ? new LoginAttemptEntity(principal,
                                    LoginAttemptEntity.LoginResult.REJECTED)
                            : new LoginAttemptEntity(retval, LoginAttemptEntity.LoginResult.SUCCESSFUL);
            persistLoginAttempt(securityAudit);
        }
        return retval == null ? null : retval.toUser();
    }

    /**
     * @param keyAlias
     * @param cryptoCert X509Certificate. Optional. Used to encrypt the new
     * secret key. If null, then system uses the user's credential cerficate.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws CertificateException
     */
    @Override
    public PublicKeyEncryptedSecretKey newSecretKey(String keyAlias) throws NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException,
            CertificateException {
        SecretKey secretKey = KeyGeneratorWrapper.generateSecretKey();
        UserEntity user = getCurrentUserImpl();
        UserSecretKeyEntity keyBase
                = new UserSecretKeyEntity(user, keyAlias, secretKey.getAlgorithm());
        persistUserSecretKey(keyBase);
        PublicKeyEncryptedSecretKeyEntity encryptedKey = new PublicKeyEncryptedSecretKeyEntity(KeyEncryptionAdapter.encrypt(secretKey, user.toUser(),
                user.getX509Certificate()), user, keyBase);
        encryptedKey.setSecretKey(keyBase);
        persistPublicKeyEncryptedSecretKey(encryptedKey);
        return encryptedKey.toPublicKeyEncryptedSecretKey();
    }

    @Override
    public void share(PublicKeyEncryptedSecretKey key, User with) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <L extends List<PublicKeyEncryptedSecretKey> & Serializable> L findUserSecretKeys() {
        L retval = (L) new ArrayList<PublicKeyEncryptedSecretKey>();
        for (PublicKeyEncryptedSecretKeyEntity entity : (List<PublicKeyEncryptedSecretKeyEntity>) em.createNamedQuery("PublicKeyEncryptedSecretKeyEntity.findByOwner").setParameter("owner",
                getCurrentUserImpl()).getResultList()) {
            retval.add(entity.toPublicKeyEncryptedSecretKey());
        }
        return retval;
    }

    @Override
    public User getCurrentUser() {
        UserEntity entity = getCurrentUserImpl();
        return entity == null ? null : entity.toUser();
    }

    @Override
    public void setUserCertificate(X509Certificate cert) throws NoSuchAlgorithmException,
            CertificateEncodingException {
        UserEntity currentUser = getCurrentUserImpl();
        currentUser.setX509Certificate(cert);
        // currentUser should be live at the moment, so the EJB container should
        // automatically post and commit the changes.
    }
}
