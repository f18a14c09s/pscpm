/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.francisjohnson.pscpm.security.services.javacrypto;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.francisjohnson.pscpm.general.services.IOUtil;

/**
 *
 * @author fjohnson
 */
public class X509Adapter {

    public static X509Certificate getX509Certificate(byte[] encoded)
            throws CertificateException {
        if (encoded == null
                || encoded.length < 1) {
            return null;
        } else {
            ByteArrayInputStream bais = null;
            try {
                bais
                        = new ByteArrayInputStream(encoded);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                return (X509Certificate) cf.generateCertificate(bais);
            } finally {
                IOUtil.closeSafely(bais);
            }
        }
    }
}
