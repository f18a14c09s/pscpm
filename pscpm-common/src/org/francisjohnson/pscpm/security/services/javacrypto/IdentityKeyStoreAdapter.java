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
package org.francisjohnson.pscpm.security.services.javacrypto;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

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
import java.util.logging.Logger;

import org.francisjohnson.pscpm.security.data.x500.CertificateProposedPurpose;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;

public class IdentityKeyStoreAdapter {

    public static final String DEFAULT_KEY_ALIAS_FILTER = "";
//    public static final String DEFAULT_KEY_ALIAS_FILTER = "JOHNSON.FRANCIS.D.1281815233";
    public static final String DEFAULT_PROVIDER_NAME = "SunMSCAPI";
    public static final String DEFAULT_IDENTITY_STORE_TYPE = "Windows-MY";

    public static Map<String, UserCredential> filterPrivateKeys(String aliasFilter) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        return filterPrivateKeys(aliasFilter, null
        //                new Date()
        );
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
            //                getLog().info("Alias: " + alias + ".");
            //                getLog().info("\tIs Key Entry: " +
            //                                   keyStore.isKeyEntry(alias) + ".");
            //                getLog().info("\tCert Is X-509: " +
            //                                   (keyStore.getCertificate(alias) instanceof
            //                                    X509Certificate) + ".");
            if (keyStore.isKeyEntry(alias)
                    && keyStore.getCertificate(alias) instanceof X509Certificate) {
                X509Certificate cert
                        = (X509Certificate) keyStore.getCertificate(alias);
                //                    getLog().info("\t\tNot Before: " +
                //                                       cert.getNotBefore() + ".");
                //                    getLog().info("\t\tNow: " + now + ".");
                //                    getLog().info("\t\tNot After: " + cert.getNotAfter() +
                //                                       ".");
                //                    getLog().info("\t\t'Not Before' Before Now: " +
                //                                       cert.getNotBefore().before(now) + ".");
                //                    getLog().info("\t\t'Not After' After Now: " +
                //                                       cert.getNotAfter().after(now) + ".");
                if (cert.getNotBefore().before(now)
                        && (minEndDate == null || cert.getNotAfter().after(minEndDate))) {
                    Key key = keyStore.getKey(alias, null);
                    //                        getLog().info("\t\t\tKey Is Private: " +
                    //                                           (key instanceof PrivateKey) + ".");
                    if (key instanceof PrivateKey) {
                        UserCredential cred
                                = new UserCredential(alias, provider, keyStore, cert,
                                        (PrivateKey) key);
                        //                        credentials.put(alias,
                        credentials.put(cred.getFriendlyName(), cred);
                        //                        getLog().info(cred.getFriendlyName());
                    }
                }
            }
        }
        getLog().info(credentials.size()
                + " total candidate credentials found.");
        return credentials;
    }

    public static UserCredential getBasicEncryptionCredential(String aliasFilter) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        return getBasicEncryptionCredential(aliasFilter,
                null
        //                , new Date()
        );
    }

    public static UserCredential getAdvancedSignatureCredential(String aliasFilter) throws KeyStoreException,
            IOException,
            NoSuchAlgorithmException,
            CertificateException,
            UnrecoverableKeyException {
        for (UserCredential cred : filterPrivateKeys(aliasFilter).values()) {
            if (cred.getPurpose().equals(CertificateProposedPurpose.ADVANCED_SIGNATURE)) {
                getLog().info("Found advanced signature credential.  Alias: "
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
                getLog().info("Found basic encryption credential.  Alias: "
                        + cred.getAlias() + ".");
                return cred;
            }
        }
        return null;
    }

    private static Logger getLog() {
        return Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    }
}
