package org.francisjohnson.pscpm.security.data.x500;

public enum X500ASN1GeneralName {
    OTHER_NAME(0, "otherName", "OtherName"),
    RFC822_NAME(1, "rfc822Name", "IA5String"),
    DNS_NAME(2, "dNSName", "IA5String"),
    X400_ADDRESS(3, "x400Address", "ORAddress"),
    DIRECTORY_NAME(4, "directoryName", "Name"),
    EDI_PARTY_NAME(5, "ediPartyName", "EDIPartyName"),
    UNIFORM_RESOURCE_IDENTIFIER(6, "uniformResourceIdentifier", "IA5String"),
    IP_ADDRESS(7, "iPAddress", "OCTET STRING"),
    REGISTERED_ID(8, "registeredID", "OBJECT IDENTIFIER");
    private int id;
    private String camelCaseName;
    private String dataType;

    private X500ASN1GeneralName(int id, String camelCaseName,
                                String dataType) {
        this.id = id;
        this.camelCaseName = camelCaseName;
        this.dataType = dataType;
    }

    public static X500ASN1GeneralName find(int id) {
        for (X500ASN1GeneralName nm : values()) {
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

    public String getDataType() {
        return dataType;
    }
}
