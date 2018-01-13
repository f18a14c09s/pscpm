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
package org.francisjohnson.pscpm.secrets.services;

import java.io.Serializable;
import java.util.List;

import org.francisjohnson.pscpm.secrets.data.CertificateSecret;
import org.francisjohnson.pscpm.secrets.data.Secret;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.general.services.IService;

public interface ISecretsService extends IService {

    <EntityClass> Secret<EntityClass> persistSecret(Secret<EntityClass> secret);

    <EntityClass> Secret<EntityClass> mergeSecret(Secret<EntityClass> secret);

    void removeSecret(Secret<?> secret);

    <L extends List<ServerSecret> & Serializable> L findServers();

    <L extends List<CertificateSecret> & Serializable> L findCertificates();

    <EntityClass> Secret<EntityClass> refresh(Secret<EntityClass> secret);

    <L extends List<ServerSecret> & Serializable> L findAvailableServerSecrets();

    ServerSecret findServer(long id);

    CertificateSecret findCertificate(long id);
}
