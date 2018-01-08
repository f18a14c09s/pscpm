/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.francisjohnson.pscpm.general.services;

import javax.ejb.EJB;
import org.francisjohnson.pscpm.secrets.services.ISecretsService;
import org.francisjohnson.pscpm.secrets.services.SecretsSvcRemoteFacade;
import org.francisjohnson.pscpm.security.services.ISecurityService;
import org.francisjohnson.pscpm.security.services.SecuritySvcRemoteFacade;
import org.francisjohnson.pscpm.security.services.SecuritySvcRemoteFacadeBean;

/**
 *
 * @author fjohnson
 */
public class ServiceFactory {

//    @EJB
//    private SecuritySvcRemoteFacade securitySvc = SecuritySvcRemoteFacadeBean.getInstance();
    @EJB
    private SecretsSvcRemoteFacade secretsSvc;

    public static ServiceFactory getInstance() {
        return new ServiceFactory();
    }

    public ISecurityService getSecuritySvc() {
        return SecuritySvcRemoteFacadeBean.getInstance();
    }

    public ISecretsService getSecretsSvc() {
        return secretsSvc;
    }
}
