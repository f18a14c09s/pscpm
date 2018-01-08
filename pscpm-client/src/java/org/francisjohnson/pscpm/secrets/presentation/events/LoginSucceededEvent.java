package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMViewId;

public class LoginSucceededEvent extends NavigationEvent {
    private transient SecretsFacade session;

    public LoginSucceededEvent(Object source, SecretsFacade session) {
        super(source, PSCPMViewId.LOGIN_FORM, PSCPMOutcome.SUCCESS);
        setSession(session);
    }

    private void setSession(SecretsFacade session) {
        this.session = session;
    }

    public SecretsFacade getSession() {
        return session;
    }
}
