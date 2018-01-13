package test.org.francisjohnson.pscpm.security.services.javacrypto;

import java.util.Map;

import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class IdentityKeyStoreAdapterTest {

    public IdentityKeyStoreAdapterTest() {
    }

    public static void main(String[] args) {
        String[] args2 = {IdentityKeyStoreAdapterTest.class.getName()};
        JUnitCore.main(args2);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter#getAdvancedSignatureCredential(String)
     */
    @Test
    public void testGetAdvancedSignatureCredential() {
        UserCredential cred = null;
        try {
            cred
                    = IdentityKeyStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
        } catch (Exception e) {
            e.printStackTrace();
            fail("See stderr.");
        }
        verifyCredential(cred);
    }

    private void verifyCredential(UserCredential cred) {
        assertNotNull(cred);
        System.out.println("Verifying UserCredential:\n\tAlias: "
                + cred.getAlias() + ";\n\tPurpose: "
                + cred.getPurpose().getLabel() + ".");
        assertNotNull(cred.getAlias());
        assertNotNull(cred.getCert());
        assertNotNull(cred.getFriendlyName());
        assertNotNull(cred.getKey());
        assertNotNull(cred.getKeyStore());
        assertNotNull(cred.getProvider());
        assertNotNull(cred.getPublicKeyBase64());
        assertNotNull(cred.getPurpose());
        try {
            assertNotNull(cred.getRfc822SubjectAlternativeName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("See stderr.");
        }
    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter#getBasicEncryptionCredential(String)
     */
    @Test
    public void testGetBasicEncryptionCredential() {
        UserCredential cred = null;
        try {
            cred
                    = IdentityKeyStoreAdapter.getBasicEncryptionCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
        } catch (Exception e) {
            e.printStackTrace();
            fail("See stderr.");
        }
        verifyCredential(cred);
    }

    /**
     * @see
     * org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter#filterPrivateKeys(String)
     */
    @Test
    public void testFilterPrivateKeys() {
        Map<String, UserCredential> creds = null;
        try {
            creds
                    = IdentityKeyStoreAdapter.filterPrivateKeys(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER);
        } catch (Exception e) {
            e.printStackTrace();
            fail("See stderr.");
        }
        assertNotNull(creds);
        for (Map.Entry<String, UserCredential> entry : creds.entrySet()) {
            assertNotNull(entry.getKey());
            verifyCredential(entry.getValue());
            assertEquals(entry.getKey(), entry.getValue().getFriendlyName());
        }
    }
}
