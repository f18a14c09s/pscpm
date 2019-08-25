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
package f18a14c09s.pscpm.general.services;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import static f18a14c09s.pscpm.general.services.IOUtil.deserialize;
import static f18a14c09s.pscpm.general.services.IOUtil.serialize;
import f18a14c09s.pscpm.secrets.data.Secret;
import f18a14c09s.pscpm.secrets.services.ISecretsService;

/**
 *
 * @author fjohnson
 */
@Path("secrets")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_OCTET_STREAM)
public class SecretsServiceJaxRsAdapter {

    private final ISecretsService secrets = ServiceFactory.getInstance().getSecretsSvc();
    private final Logger log = Logger.getLogger(getClass().getName());
    @Context
    private SecurityContext ctx;

    public SecretsServiceJaxRsAdapter() {
    }

    private ISecretsService getSecrets() {
        return secrets;
    }

    private Logger getLog() {
        return log;
    }

    private SecurityContext getCtx() {
        return ctx;
    }

    @Path("persist_secret")
    @POST
    public byte[] persistSecret(@FormParam("secret_to_persist") String base64EncodedSecret) {
        try {
            Secret<?> secret = deserialize(Base64.getDecoder().decode(base64EncodedSecret));
            return serialize(getSecrets().persistSecret(secret));
        } catch (IOException | ClassNotFoundException e) {
            getLog().log(Level.SEVERE, "Failed to refresh secret.", e);
            return new byte[0];
        }
    }

    @Path("merge_secret")
    @POST
    public byte[] mergeSecret(@FormParam("secret_to_merge") String base64EncodedSecret) {
        try {
            Secret<?> secret = deserialize(Base64.getDecoder().decode(base64EncodedSecret));
            return serialize(getSecrets().mergeSecret(secret));
        } catch (IOException | ClassNotFoundException e) {
            getLog().log(Level.SEVERE, "Failed to refresh secret.", e);
            return new byte[0];
        }
    }

    @Path("remove_secret")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String removeSecret(@FormParam("secret_to_remove") String base64EncodedSecret) {
        try {
            Secret<?> secret = deserialize(Base64.getDecoder().decode(base64EncodedSecret));
            getSecrets().removeSecret(secret);
            return "OK";
        } catch (IOException | ClassNotFoundException e) {
            getLog().log(Level.SEVERE, "Failed to refresh secret.", e);
            return "ERROR";
        }
    }

    @Path("refresh_secret")
    @POST
    public byte[] refresh(@FormParam("secret_to_refresh") String base64EncodedSecret) {
        try {
            Secret<?> secret = deserialize(Base64.getDecoder().decode(base64EncodedSecret));
            return serialize(getSecrets().refresh(secret));
        } catch (IOException | ClassNotFoundException e) {
            getLog().log(Level.SEVERE, "Failed to refresh secret.", e);
            return new byte[0];
        }
    }

    @Path("find_servers")
    @GET
    @Consumes(MediaType.WILDCARD)
    public byte[] findServers() {
        try {
            return serialize(getSecrets().findServers());
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Failed to find servers.", e);
            return new byte[0];
        }
    }

    @Path("find_certs")
    @GET
    @Consumes(MediaType.WILDCARD)
    public byte[] findCertificates() {
        try {
            return serialize(getSecrets().findCertificates());
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Failed to find certificates.", e);
            return new byte[0];
        }
    }

    @Path("find_avail_server_secrets")
    @GET
    @Consumes(MediaType.WILDCARD)
    public byte[] findAvailableServerSecrets() {
        try {
            return serialize(getSecrets().findAvailableServerSecrets());
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Failed to find available server secrets.", e);
            return new byte[0];
        }
    }

    @Path("find_server/{id}")
    @GET
    @Consumes(MediaType.WILDCARD)
    public byte[] findServer(@PathParam("id") long id) {
        try {
            return serialize(getSecrets().findServer(id));
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Failed to find server.", e);
            return new byte[0];
        }
    }

    @Path("find_cert/{id}")
    @GET
    @Consumes(MediaType.WILDCARD)
    public byte[] findCertificate(@PathParam("id") long id) {
        try {
            return serialize(getSecrets().findCertificate(id));
        } catch (IOException e) {
            getLog().log(Level.SEVERE, "Failed to find certificate.", e);
            return new byte[0];
        }
    }
}
