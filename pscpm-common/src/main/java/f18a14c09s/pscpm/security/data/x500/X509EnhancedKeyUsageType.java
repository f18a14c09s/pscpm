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
package f18a14c09s.pscpm.security.data.x500;


public enum X509EnhancedKeyUsageType {
    XCN_OID_ANY_APPLICATION_POLICY("1.3.6.1.4.1.311.10.12.1",
                                   "The applications that can use the certificate are not restricted."),
    XCN_OID_AUTO_ENROLL_CTL_USAGE("1.3.6.1.4.1.311.20.1",
                                  "The certificate can be used to sign a request for automatic enrollment in a certificate trust list (CTL)."),
    XCN_OID_DRM("1.3.6.1.4.1.311.10.5.1",
                "The certificate can be used for digital rights management applications."),
    XCN_OID_DS_EMAIL_REPLICATION("1.3.6.1.4.1.311.21.19",
                                 "The certificate can be used for Directory Service email replication."),
    XCN_OID_EFS_RECOVERY("1.3.6.1.4.1.311.10.3.4.1",
                         "The certificate can be used for recovery of documents protected by using Encrypting File System (EFS)."),
    XCN_OID_EMBEDDED_NT_CRYPTO("1.3.6.1.4.1.311.10.3.8",
                               "The certificate can be used for Windows NT Embedded cryptography."),
    XCN_OID_ENROLLMENT_AGENT("1.3.6.1.4.1.311.20.2.1",
                             "The certificate can be used by an enrollment agent."),
    XCN_OID_IPSEC_KP_IKE_INTERMEDIATE("1.3.6.1.5.5.8.2.2",
                                      "The certificate can be used for Internet Key Exchange (IKE)."),
    XCN_OID_KP_CA_EXCHANGE("1.3.6.1.4.1.311.21.5",
                           "The certificate can be used for archiving a private key on a certification authority."),
    XCN_OID_KP_CTL_USAGE_SIGNING("1.3.6.1.4.1.311.10.3.1",
                                 "The certificate can be used to sign a CTL."),
    XCN_OID_KP_DOCUMENT_SIGNING("1.3.6.1.4.1.311.10.3.12",
                                "The certificate can be used for signing documents."),
    XCN_OID_KP_EFS("1.3.6.1.4.1.311.10.3.4",
                   "The certificate can be used to encrypt files by using the Encrypting File System."),
    XCN_OID_KP_KEY_RECOVERY("1.3.6.1.4.1.311.10.3.11",
                            "The certificate can be used to encrypt and recover escrowed keys."),
    XCN_OID_KP_KEY_RECOVERY_AGENT("1.3.6.1.4.1.311.21.6",
                                  "The certificate is used to identify a key recovery agent."),
    XCN_OID_KP_LIFETIME_SIGNING("1.3.6.1.4.1.311.10.3.13",
                                "Limits the validity period of a signature to the validity period of the certificate. This restriction is typically used with the XCN_OID_PKIX_KP_CODE_SIGNING OID value to indicate that new time stamp semantics should be used."),
    XCN_OID_KP_QUALIFIED_SUBORDINATION("1.3.6.1.4.1.311.10.3.10",
                                       "The certificate can be used to sign cross certificate and subordinate certification authority certificate requests. Qualified subordination is implemented by applying basic constraints, certificate policies, and application policies. Cross certification typically requires policy mapping."),
    XCN_OID_KP_SMARTCARD_LOGON("1.3.6.1.4.1.311.20.2.2",
                               "The certificate enables an individual to log on to a computer by using a smart card."),
    XCN_OID_KP_TIME_STAMP_SIGNING("1.3.6.1.4.1.311.10.3.2",
                                  "The certificate can be used to sign a time stamp to be added to a document. Time stamp signing is typically part of a time stamping service."),
    XCN_OID_LICENSE_SERVER("1.3.6.1.4.1.311.10.6.2",
                           "The certificate can be used by a license server when transacting with Microsoft to receive licenses for Terminal Services clients."),
    XCN_OID_LICENSES("1.3.6.1.4.1.311.10.6.1",
                     "The certificate can be used for key pack licenses."),
    XCN_OID_NT5_CRYPTO("1.3.6.1.4.1.311.10.3.7",
                       "The certificate can be used for Windows Server 2003, Windows XP, and Windows 2000 cryptography."),
    XCN_OID_OEM_WHQL_CRYPTO("1.3.6.1.4.1.311.10.3.7",
                            "The certificate can be used for used for Original Equipment Manufacturers (OEM) Windows Hardware Quality Labs (WHQL) cryptography."),
    XCN_OID_PKIX_KP_CLIENT_AUTH("1.3.6.1.5.5.7.3.2",
                                "The certificate can be used for authenticating a client."),
    XCN_OID_PKIX_KP_CODE_SIGNING("1.3.6.1.5.5.7.3.3",
                                 "The certificate can be used for signing code."),
    XCN_OID_PKIX_KP_EMAIL_PROTECTION("1.3.6.1.5.5.7.3.4",
                                     "The certificate can be used to encrypt email messages."),
    XCN_OID_PKIX_KP_IPSEC_END_SYSTEM("1.3.6.1.5.5.7.3.5",
                                     "The certificate can be used for signing end-to-end Internet Protocol Security (IPSEC) communication."),
    XCN_OID_PKIX_KP_IPSEC_TUNNEL("1.3.6.1.5.5.7.3.6",
                                 "The certificate can be used for singing IPSEC communication in tunnel mode."),
    XCN_OID_PKIX_KP_IPSEC_USER("1.3.6.1.5.5.7.3.7",
                               "The certificate can be used for an IPSEC user."),
    XCN_OID_PKIX_KP_OCSP_SIGNING("1.3.6.1.5.5.7.3.9",
                                 "The certificate can be used for Online Certificate Status Protocol (OCSP) signing."),
    XCN_OID_PKIX_KP_SERVER_AUTH("1.3.6.1.5.5.7.3.1",
                                "The certificate can be used for OCSP authentication."),
    XCN_OID_PKIX_KP_TIMESTAMP_SIGNING("1.3.6.1.5.5.7.3.8",
                                      "The certificate can be used for signing public key infrastructure timestamps."),
    XCN_OID_ROOT_LIST_SIGNER("1.3.6.1.4.1.311.10.3.9",
                             "The certificate can be used to sign a certificate root list."),
    XCN_OID_WHQL_CRYPTO("1.3.6.1.4.1.311.10.3.5",
                        "The certificate can be used for Windows Hardware Quality Labs (WHQL) cryptography.");
    private String oid;
    private String description;

    private X509EnhancedKeyUsageType(String oid, String description) {
        this.oid = oid;
        this.description = description;
    }

    public String getOid() {
        return oid;
    }

    public String getDescription() {
        return description;
    }

    public static X509EnhancedKeyUsageType find(String oid) {
        for (X509EnhancedKeyUsageType nm : values()) {
            if (nm.oid.equals(oid)) {
                return nm;
            }
        }
        return null;
    }
}
