package org.francisjohnson.pscpm.security.services.javacrypto;

import java.io.IOException;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.francisjohnson.pscpm.security.data.x500.CertificateProposedPurpose;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;

public class IdentityKeyStoreAdapter {

    public static final String DEFAULT_PROVIDER_NAME = "SunMSCAPI";
    public static final String DEFAULT_IDENTITY_STORE_TYPE = "Windows-MY";

    public static Map<String, UserCredential> filterPrivateKeys(String aliasFilter) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        return filterPrivateKeys(aliasFilter, new Date());
    }

    public static Map<String, UserCredential> filterPrivateKeys(String aliasFilter, Date minEndDate) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        Map<String, UserCredential> credentials
                = new TreeMap<String, UserCredential>();
        Provider provider = Security.getProvider(DEFAULT_PROVIDER_NAME);
        KeyStore keyStore
                = KeyStore.getInstance(DEFAULT_IDENTITY_STORE_TYPE, provider);
        keyStore.load(null, null);

        Set<String> aliases = new TreeSet<String>();
        for (Enumeration<String> aliasesES = keyStore.aliases();
                aliasesES.hasMoreElements();) {
            String alias = aliasesES.nextElement();
            if (aliasFilter == null || alias.contains(aliasFilter)) {
                aliases.add(alias);
            }
        }
        Date now = new Date();
        for (String alias : aliases) {
            //                System.out.println("Alias: " + alias + ".");
            //                System.out.println("\tIs Key Entry: " +
            //                                   keyStore.isKeyEntry(alias) + ".");
            //                System.out.println("\tCert Is X-509: " +
            //                                   (keyStore.getCertificate(alias) instanceof
            //                                    X509Certificate) + ".");
            if (keyStore.isKeyEntry(alias)
                    && keyStore.getCertificate(alias) instanceof X509Certificate) {
                X509Certificate cert
                        = (X509Certificate) keyStore.getCertificate(alias);
                //                    System.out.println("\t\tNot Before: " +
                //                                       cert.getNotBefore() + ".");
                //                    System.out.println("\t\tNow: " + now + ".");
                //                    System.out.println("\t\tNot After: " + cert.getNotAfter() +
                //                                       ".");
                //                    System.out.println("\t\t'Not Before' Before Now: " +
                //                                       cert.getNotBefore().before(now) + ".");
                //                    System.out.println("\t\t'Not After' After Now: " +
                //                                       cert.getNotAfter().after(now) + ".");
                if (cert.getNotBefore().before(now)
                        && (minEndDate == null || cert.getNotAfter().after(minEndDate))) {
                    Key key = keyStore.getKey(alias, null);
                    //                        System.out.println("\t\t\tKey Is Private: " +
                    //                                           (key instanceof PrivateKey) + ".");
                    if (key instanceof PrivateKey) {
                        UserCredential cred
                                = new UserCredential(alias, provider, keyStore, cert,
                                        (PrivateKey) key);
                        //                        credentials.put(alias,
                        credentials.put(cred.getFriendlyName(), cred);
                        //                        System.out.println(cred.getFriendlyName());
                    }
                }
            }
        }
        System.out.println(credentials.size()
                + " total candidate credentials found.");
        return credentials;
    }

    public static UserCredential getBasicEncryptionCredential(String aliasFilter) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        return getBasicEncryptionCredential(aliasFilter, new Date());
    }

    public static UserCredential getAdvancedSignatureCredential(String aliasFilter) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        for (UserCredential cred : filterPrivateKeys(aliasFilter).values()) {
            if (cred.getPurpose().equals(CertificateProposedPurpose.ADVANCED_SIGNATURE)) {
                System.out.println("Found advanced signature credential.  Alias: "
                        + cred.getAlias() + ".");
                return cred;
            }
        }
        return null;
    }

    /**
     *
     * @param aliasFilter String. Filters the list of candidate credentials by
     * key alias.
     * @param maxEndDate Date. An argument that lies before the current date
     * will filter out candidate return values before they actually expire, and
     * an argument that is null or after the current date will include candidate
     * return values that have already expired.
     * @return
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     */
    public static UserCredential getBasicEncryptionCredential(String aliasFilter, Date minEndDate) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        for (UserCredential cred : filterPrivateKeys(aliasFilter, minEndDate).values()) {
            if (cred.getPurpose().equals(CertificateProposedPurpose.BASIC_ENCRYPTION)) {
                System.out.println("Found basic encryption credential.  Alias: "
                        + cred.getAlias() + ".");
                return cred;
            }
        }
        return null;
    }
}
