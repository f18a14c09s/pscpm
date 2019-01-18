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
