package org.francisjohnson.pscpm.general.services.weblogic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.security.Key;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;


/**
 * This class is so ugly and terribly despicable!  But necessary.
 *
 * The purpose of this class is to wrap the KeyStore SPI that SunMSCAPI provides.
 * The problem is that WebLogic's adapter for Java Secure Socket Extension (JSSE)
 * hard-codes some of its behaviors rather than make them configurable: in the
 * case of this application, it tries to "set" the read-only private key, which
 * the SunMSCAPI provider doesn't like.  This class converts the KeyStore SPI
 * modification methods to no-op's.
 */
public class WLSCompatibleSunMSCAPIWrapper extends Provider {
    public WLSCompatibleSunMSCAPIWrapper() {
        super("SunMSCAPI Wrapper", 0.9, "Wraps the SunMSCAPI provider.");
        try {
            Provider msCapi = Security.getProvider("SunMSCAPI");
            super.putAll(msCapi);
            super.put("KeyStore." + WLSCompatibleSunMSCAPIWrapper.WindowsMyKeyStoreWrapperSpi.MY_KEY_STORE_TYPE,
                      WindowsMyKeyStoreWrapperSpi.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final class WindowsMyKeyStoreWrapperSpi extends KeyStoreSpi {
        public static final String MY_KEY_STORE_TYPE = "Wrapper-of-Windows-MY";
        private KeyStoreSpi windowsMy;
        private KeyStoreSpi windowsRoot;

        public WindowsMyKeyStoreWrapperSpi() {
            super();
            try {
                Provider msCapi = Security.getProvider("SunMSCAPI");
                Class<KeyStoreSpi> windowsMyClass =
                    (Class<KeyStoreSpi>)Class.forName(msCapi.get("KeyStore.Windows-MY").toString());
                Class<KeyStoreSpi> windowsRootClass =
                    (Class<KeyStoreSpi>)Class.forName(msCapi.get("KeyStore.Windows-ROOT").toString());
                this.windowsMy = windowsMyClass.newInstance();
                this.windowsRoot = windowsRootClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        public WindowsMyKeyStoreWrapperSpi(KeyStoreSpi windowsMy,
                                           KeyStoreSpi windowsRoot) {
            super();
            this.windowsMy = windowsMy;
            this.windowsRoot = windowsRoot;
        }

        @Override
        public Enumeration<String> engineAliases() {
            Vector<String> aliases = new Vector<String>();
            for (Enumeration<String> oe :
                 Arrays.<Enumeration<String>>asList(windowsMy.engineAliases(),
                                                    windowsRoot.engineAliases())) {
                for (Enumeration<String> e = oe; e.hasMoreElements(); ) {
                    String alias = e.nextElement();
                    if (!aliases.contains(alias)) {
                        aliases.add(alias);
                    }
                }
            }
            return windowsMy.engineAliases();
        }

        @Override
        public boolean engineContainsAlias(String alias) {
            alias = alias.equals(MY_KEY_STORE_TYPE) ? "Windows-MY" : alias;
            return windowsMy.engineContainsAlias(alias) ||
                windowsRoot.engineContainsAlias(alias);
        }

        @Override
        public Certificate engineGetCertificate(String alias) {
            alias = alias.equals(MY_KEY_STORE_TYPE) ? "Windows-MY" : alias;
            return (windowsMy.engineContainsAlias(alias) ? windowsMy :
                    windowsRoot).engineGetCertificate(alias);
        }

        @Override
        public String engineGetCertificateAlias(Certificate cert) {
            return windowsMy.engineGetCertificateAlias(cert) == null ?
                   windowsRoot.engineGetCertificateAlias(cert) :
                   windowsMy.engineGetCertificateAlias(cert);
        }

        @Override
        public Certificate[] engineGetCertificateChain(String alias) {
            alias = alias.equals(MY_KEY_STORE_TYPE) ? "Windows-MY" : alias;
            return (windowsMy.engineContainsAlias(alias) ? windowsMy :
                    windowsRoot).engineGetCertificateChain(alias);
        }

        @Override
        public Date engineGetCreationDate(String alias) {
            alias = alias.equals(MY_KEY_STORE_TYPE) ? "Windows-MY" : alias;
            return (windowsMy.engineContainsAlias(alias) ? windowsMy :
                    windowsRoot).engineGetCreationDate(alias);
        }

        @Override
        public Key engineGetKey(String alias,
                                char[] password) throws NoSuchAlgorithmException,
                                                        UnrecoverableKeyException {
            alias = alias.equals(MY_KEY_STORE_TYPE) ? "Windows-MY" : alias;
            return (windowsMy.engineContainsAlias(alias) ? windowsMy :
                    windowsRoot).engineGetKey(alias, password);
        }

        @Override
        public boolean engineIsCertificateEntry(String alias) {
            alias = alias.equals(MY_KEY_STORE_TYPE) ? "Windows-MY" : alias;
            return (windowsMy.engineContainsAlias(alias) ? windowsMy :
                    windowsRoot).engineIsCertificateEntry(alias);
        }

        @Override
        public boolean engineIsKeyEntry(String alias) {
            alias = alias.equals(MY_KEY_STORE_TYPE) ? "Windows-MY" : alias;
            return (windowsMy.engineContainsAlias(alias) ? windowsMy :
                    windowsRoot).engineIsKeyEntry(alias);
        }

        @Override
        public void engineLoad(InputStream stream,
                               char[] password) throws IOException,
                                                       NoSuchAlgorithmException,
                                                       CertificateException {
            windowsMy.engineLoad(stream, password);
            windowsRoot.engineLoad(stream, password);
        }

        @Override
        public void engineSetCertificateEntry(String alias,
                                              Certificate cert) throws KeyStoreException {
            //            (windowsMy.engineContainsAlias(alias) ? windowsMy :
            //             windowsRoot).engineSetCertificateEntry(alias, cert);
            System.out.println(getClass().getSimpleName() +
                               ".  Skipping engineSetCertificateEntry.");
        }

        @Override
        public void engineSetKeyEntry(String alias, byte[] key,
                                      Certificate[] chain) throws KeyStoreException {
            //            (windowsMy.engineContainsAlias(alias) ? windowsMy :
            //             windowsRoot).engineSetKeyEntry(alias, key, chain);
            System.out.println(getClass().getSimpleName() +
                               ".  Skipping engineSetKeyEntry.");
        }

        @Override
        public void engineSetKeyEntry(String alias, Key key, char[] password,
                                      Certificate[] chain) throws KeyStoreException {
            //            (windowsMy.engineContainsAlias(alias) ? windowsMy :
            //             windowsRoot).engineSetKeyEntry(alias, key, password, chain);
            System.out.println(getClass().getSimpleName() +
                               ".  Skipping engineSetKeyEntry.");
        }

        @Override
        public int engineSize() {
            // TODO: Is this accurate?  May need to check alias uniqueness.
            return windowsMy.engineSize() + windowsRoot.engineSize();
        }

        @Override
        public void engineStore(OutputStream stream,
                                char[] password) throws IOException,
                                                        NoSuchAlgorithmException,
                                                        CertificateException {
            //            windowsMy.engineStore(stream, password);
            System.out.println(getClass().getSimpleName() +
                               ".  Skipping engineStore.");
        }

        @Override
        public void engineDeleteEntry(String alias) throws KeyStoreException {
            //            (windowsMy.engineContainsAlias(alias) ? windowsMy :
            //             windowsRoot).engineDeleteEntry(alias);
            System.out.println(getClass().getSimpleName() +
                               ".  Skipping engineDeleteEntry.");
        }
    }
}
