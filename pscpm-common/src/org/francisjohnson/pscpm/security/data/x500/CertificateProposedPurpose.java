package org.francisjohnson.pscpm.security.data.x500;


import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.x500.X500Principal;

import org.francisjohnson.pscpm.general.services.RdnParser;


/**
 * The label terminology contained herein (i.e. "ID" (identification),
 * "signature," and "encryption") corresponds to the three ways that
 * ActivIdentity's ActivClient software describes (i.e. via "friendly name") DoD
 * CAC certificates.
 */
public enum CertificateProposedPurpose {
    BASIC_ENCRYPTION("Encryption"),
    BASIC_IDENTIFICATION("ID"),
    ADVANCED_SIGNATURE("Signature"),
    UNKNOWN("Unknown");
    private String label;

    private CertificateProposedPurpose(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static CertificateProposedPurpose infer(X509Certificate x509) {
        X500Principal issuer = x509.getIssuerX500Principal();
        String issuerCn =
            issuer == null || issuer.getName(X500Principal.RFC2253) == null ?
            null : RdnParser.getFirstValue(issuer.getName(X500Principal.RFC2253),
                                    "cn");
        issuerCn = issuerCn == null ? null : issuerCn.trim().toLowerCase();
        Set<X500ASN1KeyUsageType> keyUsage =
            new HashSet<X500ASN1KeyUsageType>();
        boolean[] keyUsageAB = x509.getKeyUsage();
        if (keyUsageAB != null) {
            for (X500ASN1KeyUsageType typ : X500ASN1KeyUsageType.values()) {
                if (typ.getId() < keyUsageAB.length &&
                    keyUsageAB[typ.getId()]) {
                    keyUsage.add(typ);
                }
            }
        }
        if (keyUsage.contains(X500ASN1KeyUsageType.DIGITAL_SIGNATURE) &&
            keyUsage.contains(X500ASN1KeyUsageType.NON_REPUDIATION)) {
            Set<X509EnhancedKeyUsageType> extendedUsage =
                new HashSet<X509EnhancedKeyUsageType>();
            try {
                List<String> extendedUsageLS = x509.getExtendedKeyUsage();
                if (extendedUsageLS != null) {
                    for (String useS : x509.getExtendedKeyUsage()) {
                        X509EnhancedKeyUsageType use = X509EnhancedKeyUsageType.find(useS);
                        if (use != null) {
                            extendedUsage.add(use);
                        }
                    }
                }
            } catch (CertificateParsingException e) {
                e.printStackTrace();
            }
            if (
                // This is the only difference between CACs and IGC smart cards: the former have
                // smart card logon as an extended usage.
                //            extendedUsage.contains(X509EnhancedKeyUsageType.XCN_OID_KP_SMARTCARD_LOGON) &&
                extendedUsage.contains(X509EnhancedKeyUsageType.XCN_OID_PKIX_KP_CLIENT_AUTH) &&
                extendedUsage.contains(X509EnhancedKeyUsageType.XCN_OID_PKIX_KP_EMAIL_PROTECTION)) {
                return ADVANCED_SIGNATURE;
            }
            //                }
            return BASIC_IDENTIFICATION;
        }
        if (keyUsage.contains(X500ASN1KeyUsageType.KEY_ENCIPHERMENT) ||
            keyUsage.contains(X500ASN1KeyUsageType.DATA_ENCIPHERMENT)) {
            return BASIC_ENCRYPTION;
        } else {
            return UNKNOWN;
        }
    }
}
