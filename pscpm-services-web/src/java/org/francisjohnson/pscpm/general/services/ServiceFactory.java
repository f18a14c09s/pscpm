/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.francisjohnson.pscpm.general.services;

import org.francisjohnson.pscpm.secrets.services.ISecretsService;
import org.francisjohnson.pscpm.secrets.services.SecretsSvcRemoteFacadeBean;
import org.francisjohnson.pscpm.security.services.ISecurityService;
import org.francisjohnson.pscpm.security.services.SecuritySvcRemoteFacadeBean;

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
