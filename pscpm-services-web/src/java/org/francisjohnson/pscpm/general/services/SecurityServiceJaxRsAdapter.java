/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.francisjohnson.pscpm.general.services;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.NonUniqueResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.SecurityPrincipal;
import org.francisjohnson.pscpm.security.data.User;
import org.francisjohnson.pscpm.security.services.ISecurityService;
import static org.francisjohnson.pscpm.general.services.IOUtil.serialize;
import static org.francisjohnson.pscpm.general.services.IOUtil.deserialize;
import org.francisjohnson.pscpm.security.services.javacrypto.X509Adapter;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author fjohnson
 */
@Path("security")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class SecurityServiceJaxRsAdapter {

    private final ISecurityService security = ServiceFactory.getInstance().getSecuritySvc();
    private final Logger log = Logger.getLogger(getClass().getName());
    @Context
    private SecurityContext ctx;

    public SecurityServiceJaxRsAdapter() {
    }

    private ISecurityService getSecurity() {
        return security;
    }

    private Logger getLog() {
        return log;
    }

    private SecurityContext getCtx() {
        return ctx;
    }

    @Path("share_key")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String share(@FormParam("secret_key") String base64EncodedKey,
            @FormParam("dest_user") String destUser
    ) {
//        PublicKeyEncryptedSecretKey key=null;
//        User withU=null;
        throw new UnsupportedOperationException();
    }

    @Path("set_user_cert")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String setUserCertificate(@FormParam("cert_to_set") String base64EncodedX509Cert) //            throws NoSuchAlgorithmException,
    //            CertificateException
    {
        try {
            X509Certificate cert = X509Adapter.getX509Certificate(Base64.getDecoder().decode(base64EncodedX509Cert));
            getSecurity().setUserCertificate(cert);
            return "OK";
        } catch (NoSuchAlgorithmException
                | CertificateException e) {
            getLog().log(Level.SEVERE, "Failed to save user certificate.", e);
            return "ERROR";
        }
    }

    @Path("merge_user")
    @POST
    public byte[] mergeUser(@FormParam("user_to_merge") String base64EncodedUser) throws IOException, ClassNotFoundException {
        User user = deserialize(Base64.getDecoder().decode(base64EncodedUser));
        user = getSecurity().mergeUser(user);
        if (user == null) {
            return new byte[0];
        } else {
            try {
                return serialize(user);
            } catch (IOException e) {
                getLog().log(Level.SEVERE, "Failed to save user.", e);
                return new byte[0];
            }
        }
    }

    @Path("find_principal")
    @POST
    public byte[] findPrincipal(@FormParam("user_cert") String base64EncodedX509Cert) //            throws IOException,
    //            CertificateException, NoSuchAlgorithmException
    {
        try {
            X509Certificate cert = X509Adapter.getX509Certificate(Base64.getDecoder().decode(base64EncodedX509Cert));
            SecurityPrincipal principal = getSecurity().findPrincipal(cert);
            if (principal == null) {
                return new byte[0];
            } else {
                return serialize(principal);
            }
        } catch (IOException
                | CertificateException | NoSuchAlgorithmException e) {
            getLog().log(Level.SEVERE, "Failed to find security principal.", e);
            return new byte[0];
        }
    }

    @Path("authenticate")
    @POST
    public byte[] authenticate(@FormParam("user_cert") String base64EncodedX509Cert) //            throws CertificateException,
    //            NoSuchAlgorithmException,
    //            NonUniqueResultException, IOException
    {
//        System.out.println(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "Certificate: " + base64EncodedX509Cert));
        try {
            X509Certificate cert = base64EncodedX509Cert == null || base64EncodedX509Cert.trim().isEmpty() ? null : X509Adapter.getX509Certificate(Base64.getDecoder().decode(base64EncodedX509Cert));
            User user = getSecurity().authenticate(cert);
//            System.out.println(String.format("%s.%s: %s.", getClass().getName(), "authenticate", "Returned user: " + user + "."));
            if (user == null) {
                return new byte[0];
            } else {
                return serialize(user);
            }
        } catch (NonUniqueResultException | IOException
                | CertificateException | NoSuchAlgorithmException e) {
            getLog().log(Level.SEVERE, "Failed to find authenticate user.", e);
            return new byte[0];
        }
    }

    /**
     * @param keyAlias
     * @param cryptoCert X509Certificate. Optional. Used to encrypt the new
     * secret key. If null, then system uses the user's credential cerficate.
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws CertificateException
     */
    @Path("new_secret_key")
    @POST
    public byte[] newSecretKey(@FormParam("key_alias") String keyAlias) //            throws NoSuchAlgorithmException,
    //            NoSuchPaddingException,
    //            InvalidKeyException,
    //            IllegalBlockSizeException,
    //            BadPaddingException,
    //            CertificateException, IOException 
    {
        try {
            PublicKeyEncryptedSecretKey encryptedKey = getSecurity().newSecretKey(keyAlias);
            if (encryptedKey == null) {
                return new byte[0];
            } else {
                return serialize(encryptedKey);
            }
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | CertificateException e) {
            getLog().log(Level.SEVERE, "Failed to create new secret key.", e);
            return new byte[0];
        }
    }

    @Path("find_user_secret_keys")
    @GET
    @Consumes(MediaType.WILDCARD)
    public <L extends List<PublicKeyEncryptedSecretKey> & Serializable> byte[] findUserSecretKeys() //            throws IOException 
    {
        try {
            L keys = getSecurity().findUserSecretKeys();
            if (keys == null) {
                return new byte[0];
            } else {
                return serialize(keys);
            }
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Failed to find user's secret keys.", e);
            return new byte[0];
        }
    }

    @Path("get_current_user")
    @GET
    @Consumes(MediaType.WILDCARD)
    public byte[] getCurrentUser() //            throws IOException 
    {
        try {
            User user = getSecurity().getCurrentUser();
            if (user == null) {
                return new byte[0];
            } else {
                return serialize(user);
            }
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Failed to get the current user.", e);
            return new byte[0];
        }
    }

    @Path("whoami")
    @GET
    @Consumes(MediaType.WILDCARD)
    public String whoAmI() {
        try {
            return getCtx().getUserPrincipal().getName();
        } catch (Exception e) {
            getLog().log(Level.SEVERE, "Unable to determine who the current principal is.", e);
            return null;
        }
    }
}
