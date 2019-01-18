package test.org.francisjohnson.pscpm.security;

import org.francisjohnson.pscpm.security.business.SecurityFacade;

import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;


public class SecurityFacadeTest {
    private SecurityFacade subject;

    public static void main(String[] args) {
        String[] args2 = { SecurityFacadeTest.class.getName() };
        JUnitCore.main(args2);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * @see SecurityFacade#updateProfile(org.francisjohnson.pscpm.security.domain.User)
     */
    @Test
    public void testUpdateProfile() {
        fail("Unimplemented");
    }

    /**
     * @see SecurityFacade#findMySecretKeys()
     */
    @Test
    public void testFindMySecretKeys() {
        fail("Unimplemented");
    }

    /**
     * @see SecurityFacade#encryptAndDestroy(char[],java.security.cert.X509Certificate)
     */
    @Test
    public void testEncryptAndDestroy() {
        fail("Unimplemented");
    }

    /**
     * @see SecurityFacade#decryptPassword(org.francisjohnson.pscpm.security.domain.PublicKeyEncryptedKey)
     */
    @Test
    public void testDecryptPassword() {
        fail("Unimplemented");
    }

    /**
     * @see SecurityFacade#generatePassword()
     */
    @Test
    public void testGeneratePassword() {
        fail("Unimplemented");
    }

    /**
     * @see SecurityFacade#login()
     */
    @Test
    public void testLogin() {
        fail("Unimplemented");
    }

    private void setSubject(SecurityFacade subject) {
        this.subject = subject;
    }

    private SecurityFacade getSubject() {
        return subject;
    }
}
