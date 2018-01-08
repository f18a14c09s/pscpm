package org.francisjohnson.pscpm.security.data.x500;


public enum X500ASN1KeyUsageType {
    DIGITAL_SIGNATURE(0, "digitalSignature"),
    NON_REPUDIATION(1, "nonRepudiation"),
    KEY_ENCIPHERMENT(2, "keyEncipherment"),
    DATA_ENCIPHERMENT(3, "dataEncipherment"),
    KEY_AGREEMENT(4, "keyAgreement"),
    KEY_CERT_SIGN(5, "keyCertSign"),
    CRL_SIGN(6, "cRLSign"),
    ENCIPHER_ONLY(7, "encipherOnly"),
    DECIPHER_ONLY(8, "decipherOnly");
    private int id;
    private String camelCaseName;

    private X500ASN1KeyUsageType(int id, String camelCaseName) {
        this.id = id;
        this.camelCaseName = camelCaseName;
    }

    public static X500ASN1KeyUsageType find(int id) {
        for (X500ASN1KeyUsageType nm : values()) {
            if (nm.id == id) {
                return nm;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getCamelCaseName() {
        return camelCaseName;
    }
}
