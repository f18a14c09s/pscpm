package test.org.francisjohnson.pscpm.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;

import java.util.List;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.francisjohnson.pscpm.general.services.ServiceFacade;
import org.francisjohnson.pscpm.secrets.data.ServerEnvironment;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.security.services.ISecurityService;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;

import org.junit.After;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import test.org.francisjohnson.pscpm.PSCPMTestCase;

public class SecurityServiceTest extends PSCPMTestCase {

    UserCredential encryptionCredential;
    UserCredential authenticationCredential;
    private ISecurityService subject;

    public static void main(String[] args) {
        String[] args2 = {SecurityServiceTest.class.getName()};
        JUnitCore.main(args2);
    }

    @Before
    public void setUp() throws Exception {
        setEncryptionCredential(IdentityKeyStoreAdapter.getBasicEncryptionCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
        setAuthenticationCredential(IdentityKeyStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
        setSubject((ISecurityService) ServiceFacade.<ISecurityService>getInstance(ISecurityService.class, ServiceFacade.DEFAULT_SERVER_URL,
                getAuthenticationCredential()).newFacade());
        getSubject().authenticate(getAuthenticationCredential().getCert());
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.ISecurityService#authenticate(java.security.cert.X509Certificate)
     */
    @Test
    public void testAuthenticate() {
        assertNotNull(getSubject());
        assertNotNull(getSubject().getCurrentUser());
        fail("Unimplemented");
    }

//    /**
//     * @see
//     * org.francisjohnson.pscpm.security.services.ISecurityService#findPrincipal(java.security.cert.X509Certificate)
//     */
//    @Test
//    public void testFindPrincipal() {
//        fail("Unimplemented");
//    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.ISecurityService#newSecretKey(String)
     */
    @Test
    public void testNewSecretKey() {
        long uniqueId = System.currentTimeMillis();
        try {
            for (ServerEnvironment env
                    : new ServerEnvironment[]{ServerEnvironment.DEVELOPMENT, ServerEnvironment.TEST, ServerEnvironment.PRODUCTION}) {
                //                for (Credential.Type sharedAcctType :
                //                     new Credential.Type[] { Credential.Type.APPLICATION,
                //                                             Credential.Type.ADMINISTRATIVE }) {
                assertNotNull("Non-null return value expected.",
                        getSubject().newSecretKey(env + " "
                                + //                                                        sharedAcctType +
                                "Shared" + " Accts " + uniqueId));
                //                }
                assertNotNull("Non-null return value expected.",
                        getSubject().newSecretKey(env + " " + org.francisjohnson.pscpm.secrets.data.UserAccount.Type.INDIVIDUAL
                                + " Accts " + uniqueId));
            }
        } catch (CertificateException e) {
            failWithException(e);
        } catch (BadPaddingException e) {
            failWithException(e);
        } catch (IllegalBlockSizeException e) {
            failWithException(e);
        } catch (InvalidKeyException e) {
            failWithException(e);
        } catch (NoSuchPaddingException e) {
            failWithException(e);
        } catch (NoSuchAlgorithmException e) {
            failWithException(e);
        }
    }

//    /**
//     * @see
//     * org.francisjohnson.pscpm.security.services.ISecurityService#share(org.francisjohnson.pscpm.security.domain.PublicKeyEncryptedSecretKey,org.francisjohnson.pscpm.security.domain.User)
//     */
//    @Test
//    public void testShare() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.security.services.ISecurityService#findUserSecretKeys()
//     */
//    @Test
//    public void testFindUserSecretKeys() {
//        fail("Unimplemented");
//    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.ISecurityService#getCurrentUser()
     */
    @Test
    public void testGetCurrentUser() {
        assertNotNull(getSubject().getCurrentUser());
    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.ISecurityService#setUserCertificate(java.security.cert.X509Certificate)
     */
    @Test
    public void testSetUserCertificate() {
        try {
            getSubject().setUserCertificate(getEncryptionCredential().getCert());
        } catch (CertificateEncodingException e) {
            failWithException(e);
        } catch (NoSuchAlgorithmException e) {
            failWithException(e);
        }
    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.ISecurityService#mergeUser(org.francisjohnson.pscpm.security.domain.User)
     */
    @Test
    public void testMergeUser() {
        List<PublicKeyEncryptedSecretKey> keys
                = getSubject().findUserSecretKeys();
        Random rand = new Random();
        PublicKeyEncryptedSecretKey secretKey
                = keys.get(rand.nextInt(keys.size()));
        User currentUser = getSubject().getCurrentUser();
        currentUser.setDefaultSecretKey(secretKey);
        getSubject().mergeUser(currentUser);
    }

    private void setSubject(ISecurityService security) {
        this.subject = security;
    }

    private ISecurityService getSubject() {
        return subject;
    }

    private void setEncryptionCredential(UserCredential encryptionCredential) {
        this.encryptionCredential = encryptionCredential;
    }

    private UserCredential getEncryptionCredential() {
        return encryptionCredential;
    }

    private void setAuthenticationCredential(UserCredential authenticationCredential) {
        this.authenticationCredential = authenticationCredential;
    }

    private UserCredential getAuthenticationCredential() {
        return authenticationCredential;
    }
}
