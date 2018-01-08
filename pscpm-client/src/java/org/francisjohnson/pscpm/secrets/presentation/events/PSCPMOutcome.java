package org.francisjohnson.pscpm.secrets.presentation.events;

import org.francisjohnson.pscpm.general.data.BreadAction;

public enum PSCPMOutcome {
    DONE,
    SUCCESS,
    FAILURE,
    BROWSE_SERVERS(BreadAction.BROWSE, "Server Secret Browser"),
    ADD_SERVER(BreadAction.ADD, "Server Secret Add"),
    // Feel free to change to EDIT.  Doing so will imply that the default mode
    // is to edit the server.
    MANAGE_SERVER(BreadAction.READ, "Server Secret Mgmt"),
    ADD_ACCOUNT(BreadAction.ADD, "User Account Add"),
    // Feel free to change to EDIT.  Doing so will imply that the default mode
    // is to edit the account.
    MANAGE_ACCOUNT(BreadAction.READ, "User Account Mgmt"),
    LOGOUT;
    private BreadAction action;
    private String featureName;

    private PSCPMOutcome() {
    }

    private PSCPMOutcome(BreadAction action) {
        setAction(action);
    }

    private PSCPMOutcome(BreadAction action, String functionalityDisplayName) {
        setAction(action);
        setFeatureName(functionalityDisplayName);
    }

    private void setAction(BreadAction action) {
        this.action = action;
    }

    public BreadAction getAction() {
        return action;
    }

    private void setFeatureName(String functionalityDisplayName) {
        this.featureName = functionalityDisplayName;
    }

    public String getFeatureName() {
        return featureName;
    }
}
