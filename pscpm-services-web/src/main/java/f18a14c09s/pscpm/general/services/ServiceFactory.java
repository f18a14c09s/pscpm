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

import f18a14c09s.pscpm.secrets.services.ISecretsService;
import f18a14c09s.pscpm.secrets.services.SecretsSvcRemoteFacadeBean;
import f18a14c09s.pscpm.security.services.ISecurityService;
import f18a14c09s.pscpm.security.services.SecuritySvcRemoteFacadeBean;

/**
 *
 * @author fjohnson
 */
public class ServiceFactory {
    public static ServiceFactory getInstance() {
        return new ServiceFactory();
    }

    public ISecurityService getSecuritySvc() {
        return SecuritySvcRemoteFacadeBean.getInstance();
    }

    public ISecretsService getSecretsSvc() {
        return SecretsSvcRemoteFacadeBean.getInstance();
    }
}
