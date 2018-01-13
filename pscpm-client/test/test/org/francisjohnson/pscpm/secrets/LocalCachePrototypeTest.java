package test.org.francisjohnson.pscpm.secrets;

import java.io.IOException;

import org.francisjohnson.pscpm.general.services.ServiceFacade;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.secrets.services.ISecretsService;
import org.francisjohnson.pscpm.secrets.services.FileAndObjectIOAdapter;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.services.ISecurityService;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;

import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import test.org.francisjohnson.pscpm.PSCPMTestCase;

public class LocalCachePrototypeTest extends PSCPMTestCase {

    private ISecretsService secretsSvc;
    private ISecurityService securitySvc;

    public LocalCachePrototypeTest() {
    }

    public static void main(String[] args) {
        String[] args2 = {LocalCachePrototypeTest.class.getName()};
        JUnitCore.main(args2);
    }

    @Before
    public void setUp() throws Exception {
        ServiceFacade<ISecretsService> factory = ServiceFacade.<ISecretsService>getInstance(ISecretsService.class, ServiceFacade.DEFAULT_SERVER_URL, IdentityKeyStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
        ServiceFacade<ISecurityService> fac1 = ServiceFacade.<ISecurityService>getInstance(ISecurityService.class, ServiceFacade.DEFAULT_SERVER_URL, IdentityKeyStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
        setSecretsSvc(factory.newFacade());
        setSecuritySvc(fac1.newFacade());
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * @see
     * org.francisjohnson.pscpm.secrets.services.LocalCachePrototype#cache(SerIdent)
     */
    @Test
    public void testCache() {
        try {
            for (PublicKeyEncryptedSecretKey objectToCache
                    : getSecuritySvc().findUserSecretKeys()) {
                FileAndObjectIOAdapter.cache(objectToCache);
            }
            for (ServerSecret objectToCache
                    : getSecretsSvc().findAvailableServerSecrets()) {
                FileAndObjectIOAdapter.cache(objectToCache);
            }
        } catch (ClassNotFoundException e) {
            failWithException(e);
        } catch (IOException e) {
            failWithException(e);
        }
    }

    /**
     * @see
     * org.francisjohnson.pscpm.secrets.services.LocalCachePrototype#loadAllByInterface(Class)
     */
    @Test
    public void testLoadAllByInterface() {
        fail("Unimplemented");
    }

    /**
     * @see
     * org.francisjohnson.pscpm.secrets.services.LocalCachePrototype#loadAllByClass(Class)
     */
    @Test
    public void testLoadAllByClass() {
        fail("Unimplemented");
    }

    private void setSecretsSvc(ISecretsService secretsService) {
        this.secretsSvc = secretsService;
    }

    private ISecretsService getSecretsSvc() {
        return secretsSvc;
    }

    private void setSecuritySvc(ISecurityService security) {
        this.securitySvc = security;
    }

    private ISecurityService getSecuritySvc() {
        return securitySvc;
    }
}
