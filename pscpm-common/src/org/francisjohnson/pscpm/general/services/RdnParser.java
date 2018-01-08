package org.francisjohnson.pscpm.general.services;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;


public class RdnParser {
    public static String getFirstValue(String dnString, String rdnType) {
        try {
            LdapName dn = new LdapName(dnString);
            for (Rdn rdn : dn.getRdns()) {
                if (rdn.getType().equalsIgnoreCase(rdnType)) {
                    return rdn.getValue().toString();
                }
            }
        } catch (InvalidNameException e) {
            e.printStackTrace();
        }
        return null;
    }
}
