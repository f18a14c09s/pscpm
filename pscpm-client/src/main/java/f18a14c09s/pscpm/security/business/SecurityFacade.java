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
package f18a14c09s.pscpm.security.business;

import java.security.cert.X509Certificate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import f18a14c09s.pscpm.security.data.SecurityException;
import f18a14c09s.pscpm.security.data.javacrypto.UserCredential;
import f18a14c09s.pscpm.security.services.ISecurityService;
import f18a14c09s.pscpm.security.services.PasswordGenerator;
import f18a14c09s.pscpm.security.services.javacrypto.KeyDecryptionAdapter;
import f18a14c09s.pscpm.security.services.javacrypto.KeyEncryptionAdapter;
import f18a14c09s.pscpm.general.services.ServiceFacade;
import f18a14c09s.pscpm.secrets.services.FileAndObjectIOAdapter;
import f18a14c09s.pscpm.security.data.OfflineUser;
import f18a14c09s.pscpm.security.data.PublicKeyEncryptedKey;
import f18a14c09s.pscpm.security.data.PublicKeyEncryptedSecretKey;
import f18a14c09s.pscpm.security.data.User;


public class SecurityFacade {
    private Logger logger =
        Logger.getLogger(this.getClass().getPackage().getName());
    private ISecurityService security;
    private OfflineUser offlineUser = new OfflineUser();

    public SecurityFacade()
        //        throws SecurityException
    {
        super();
        //            try {
        //                setSecurity(RemoteFacadeFactory.<ISecurityService>getInstance(ISecurityService.class,
        //                                                                              WebLogicRMIPrototype.DEFAULT_RMI_PROVIDER_URL,
        //                                                                              PublicKeyIdentityStoreAdapter.getAdvancedSignatureCredential()).newFacade());
        //            } catch (Exception e) {
        //                throw new SecurityException("Unable to contact security service.",
        //                                            e);
        //            }
    }

    public boolean isOffline() {
        return getOfflineUser() != null;
    }

    public SecurityFacade(String providerUrl,
                          UserCredential cred) throws SecurityException {
        super();
        try {
            setSecurity((ISecurityService)ServiceFacade.<ISecurityService>getInstance(ISecurityService.class,
                                                                                      providerUrl,
                                                                                      cred).newFacade());
            setOfflineUser(null);
        } catch (Exception e) {
            throw new SecurityException("Unable to contact security service.",
                                        e);
        }
    }

    public List<PublicKeyEncryptedSecretKey> findMySecretKeys() throws SecurityException {
        try {
            List<PublicKeyEncryptedSecretKey> retval = null;
            if (isOffline()) {
                retval =
                        FileAndObjectIOAdapter.loadAllByInterface(PublicKeyEncryptedSecretKey.class);
            } else {
                retval = getSecurity().findUserSecretKeys();
                FileAndObjectIOAdapter.cache(retval);
            }
            return retval;
        } catch (Exception e) {
            throw new SecurityException("Unable to find user's secret keys.",
                                        e);
        } finally {
        }
    }

    public User login() throws SecurityException {
        try {
            return isOffline() ? getOfflineUser() :
                   getSecurity().getCurrentUser();
        } catch (Exception e) {
            throw new SecurityException("Login failed.", e);
        } finally {
        }
    }

    private void setSecurity(ISecurityService security) {
        this.security = security;
    }

    private ISecurityService getSecurity() {
        return security;
    }

    public void updateProfile(User user) throws SecurityException {
        if (isOffline()) {
            throw new SecurityException("Currently offline.  Profile update not available.");
        } else {
            getSecurity().mergeUser(user);
        }
    }

    /**
     * @deprecated Because the system supports sharing and one cannot share a
     * public-key-encrypted password without sharing the private key.
     * @param password
     * @param cryptoCert
     * @return
     * @throws SecurityException
     */
    @Deprecated
    public PublicKeyEncryptedKey encryptAndDestroy(char[] password,
                                                   X509Certificate cryptoCert) throws SecurityException {
        try {
            PublicKeyEncryptedKey retval =
                KeyEncryptionAdapter.encrypt(password, cryptoCert);
            Arrays.fill(password, '*');
            return retval;
        } catch (Exception e) {
            throw new SecurityException("Unable to encrypt the password.  Note: The password was not destroyed.",
                                        e);
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
    public char[] decryptPassword(PublicKeyEncryptedKey encryptedPassword) throws SecurityException {
        try {
            return KeyDecryptionAdapter.decryptPassword(encryptedPassword);
        } catch (Exception e) {
            throw new SecurityException("Unable to decrypt the encrypted password.",
                                        e);
        } finally {
        }
    }

    public char[] generatePassword() {
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword();
    }

    private void setOfflineUser(OfflineUser offlineUser) {
        this.offlineUser = offlineUser;
    }

    private OfflineUser getOfflineUser() {
        return offlineUser;
    }

    private void setLogger(Logger logger) {
        this.logger = logger;
    }

    private Logger getLogger() {
        return logger;
    }

    public void logout() {
        setOfflineUser(new OfflineUser());
        setSecurity(null);
    }
}
