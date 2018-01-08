package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMViewId;

public interface NavigationListener extends PSCPMListener {
    void navigate(NavigationEvent evt);

    void navigate(PSCPMViewId toViewId);
}
