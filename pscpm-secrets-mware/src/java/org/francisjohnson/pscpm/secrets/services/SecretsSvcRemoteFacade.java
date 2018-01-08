package org.francisjohnson.pscpm.secrets.services;

import javax.ejb.Remote;


/**
 * This facade is remote in the sense that it is accessed remotely by the client
 * tier.  The client tier's alternative is to use a cache DAO which serializes
 * objects to the local file system.  The terms "remote" and "local" in this
 * sense are not analogous to EJB's traditional remote and local homes/interfaces.
 */
@Remote
public interface SecretsSvcRemoteFacade extends ISecretsService {
}
