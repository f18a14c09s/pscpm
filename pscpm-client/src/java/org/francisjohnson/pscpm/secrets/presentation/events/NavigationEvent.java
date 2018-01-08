package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.secrets.presentation.views.PSCPMViewId;

public class NavigationEvent extends PSCPMEvent {
    private PSCPMViewId fromViewId;
    private PSCPMOutcome fromOutcome;

    public NavigationEvent(Object source, PSCPMViewId fromViewId,
                           PSCPMOutcome fromOutcome) {
        super(source);
        setFromViewId(fromViewId);
        setFromOutcome(fromOutcome);
    }

    protected void setFromViewId(PSCPMViewId fromViewId) {
        this.fromViewId = fromViewId;
    }

    public PSCPMViewId getFromViewId() {
        return fromViewId;
    }

    protected void setFromOutcome(PSCPMOutcome fromOutcome) {
        this.fromOutcome = fromOutcome;
    }

    public PSCPMOutcome getFromOutcome() {
        return fromOutcome;
    }
}
