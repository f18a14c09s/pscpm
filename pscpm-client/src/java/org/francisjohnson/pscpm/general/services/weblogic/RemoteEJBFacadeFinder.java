package org.francisjohnson.pscpm.general.services.weblogic;

import javax.ejb.Stateful;
import javax.ejb.Stateless;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class RemoteEJBFacadeFinder {
    public static <Facade> Facade getFacade(Context jndiContext,
                                            Class<Facade> impl) throws NamingException {
        String sessionBeanName = null;
        String mappedName = null;
        if (impl.getAnnotation(Stateful.class) != null) {
            sessionBeanName = impl.getAnnotation(Stateful.class).name();
            mappedName = impl.getAnnotation(Stateful.class).mappedName();
        } else if (impl.getAnnotation(Stateless.class) != null) {
            sessionBeanName = impl.getAnnotation(Stateless.class).name();
            mappedName = impl.getAnnotation(Stateless.class).mappedName();
        } else {
            throw new NamingException("Neither Stateful nor Stateless session bean annotation found.");
        }
        return (Facade)jndiContext.lookup(mappedName + "#" +
                                          (impl.getPackage() == null ||
                                           impl.getPackage().getName() ==
                                           null ||
                                           impl.getPackage().getName().isEmpty() ?
                                           "" :
                                           impl.getPackage().getName() + ".") +
                                          sessionBeanName);
    }
}
