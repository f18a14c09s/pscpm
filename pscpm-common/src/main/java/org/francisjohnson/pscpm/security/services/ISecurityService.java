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
package org.francisjohnson.pscpm.security.services;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.SecurityPrincipal;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.general.services.IService;

public interface ISecurityService extends IService {

    User authenticate(X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException;

    SecurityPrincipal findPrincipal(X509Certificate cert) throws CertificateEncodingException,
            NoSuchAlgorithmException;

    PublicKeyEncryptedSecretKey newSecretKey(String keyAlias)
            //                      , X509Certificate cert
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, CertificateException;

    void share(PublicKeyEncryptedSecretKey key, User with);

    <L extends List<PublicKeyEncryptedSecretKey> & Serializable> L findUserSecretKeys();

    User getCurrentUser();

    void setUserCertificate(X509Certificate cert) throws NoSuchAlgorithmException,
            CertificateEncodingException;

    User mergeUser(User user);
}
