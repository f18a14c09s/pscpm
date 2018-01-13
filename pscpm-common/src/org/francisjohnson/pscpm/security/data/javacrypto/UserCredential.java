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
package org.francisjohnson.pscpm.security.data.javacrypto;

import java.io.Serializable;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.francisjohnson.pscpm.security.data.x500.CertificateProposedPurpose;
import org.francisjohnson.pscpm.security.data.x500.X500ASN1GeneralName;
import org.francisjohnson.pscpm.general.services.RdnParser;


public class UserCredential implements Serializable {
    private String alias;
    private Provider provider;
    private transient KeyStore keyStore;
    private X509Certificate cert;
    private PrivateKey key;

    public UserCredential(String alias, Provider provider, KeyStore keyStore,
                          X509Certificate cert, PrivateKey key) {
        setAlias(alias);
        setProvider(provider);
        setKeyStore(keyStore);
        setCert(cert);
        setKey(key);
    }

    private void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    private void setCert(X509Certificate cert) {
        this.cert = cert;
    }

    public X509Certificate getCert() {
        return cert;
    }

    private void setKey(PrivateKey key) {
        this.key = key;
    }

    public PrivateKey getKey() {
        return key;
    }

    private void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getPublicKeyBase64() {
        return getCert() == null || getCert().getPublicKey() == null ||
               getCert().getPublicKey().getEncoded() == null ? null :
               Base64.getEncoder().encodeToString(getCert().getPublicKey().getEncoded());
    }

    public String getPublicKeyFingerprintBase64(String messageDigestAlgorithm) throws NoSuchAlgorithmException {
        if (getCert() == null || getCert().getPublicKey() == null ||
            getCert().getPublicKey().getEncoded() == null) {
            return null;
        } else {
            MessageDigest digest =
                MessageDigest.getInstance(messageDigestAlgorithm);
            return Base64.getEncoder().encodeToString(digest.digest(getCert().getPublicKey().getEncoded()));
        }
    }

    public byte[] getPublicKeyFingerprint(String messageDigestAlgorithm) throws NoSuchAlgorithmException {
        if (getCert() == null || getCert().getPublicKey() == null ||
            getCert().getPublicKey().getEncoded() == null) {
            return null;
        } else {
            MessageDigest digest =
                MessageDigest.getInstance(messageDigestAlgorithm);
            return digest.digest(getCert().getPublicKey().getEncoded());
        }
    }

    public String getFriendlyName() {
        if (getCert() == null) {
            return null;
        } else {
            CertificateProposedPurpose purpose = CertificateProposedPurpose.infer(getCert());

            String cn = RdnParser.getFirstValue(getCert().getSubjectX500Principal().getName(X500Principal.RFC2253),
                                        "cn");
            String org = RdnParser.getFirstValue(getCert().getSubjectX500Principal().getName(X500Principal.RFC2253),
                                        "o");
            return cn + "'s " + org + " " + purpose.getLabel() +
                " Certificate";
        }
    }

    public CertificateProposedPurpose getPurpose() {
        return CertificateProposedPurpose.infer(getCert());
    }

    private void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public String getRfc822SubjectAlternativeName() throws CertificateException {
        List<String> rfc822Names = new ArrayList<String>();
        for (List<?> subAltName :
             (Collection<List<?>>)getCert().getSubjectAlternativeNames()) {
            X500ASN1GeneralName generalName = X500ASN1GeneralName.find(((Number)subAltName.get(0)).intValue());
            if (generalName == X500ASN1GeneralName.RFC822_NAME) {
                String r8n = (String)subAltName.get(1);
                if (r8n != null && !r8n.isEmpty()) {
                    rfc822Names.add(r8n);
                }
            }
        }
        if (rfc822Names.size() > 1) {
            System.out.println("Multiple subject alternative RFC 822 names found: " +
                               rfc822Names + ".");
        }
        return rfc822Names.size() != 1 ? null : rfc822Names.get(0);
    }

    public String toString() {
        return getFriendlyName() == null || getFriendlyName().isEmpty() ?
               "Alias " + getAlias() : getFriendlyName();
    }
}
