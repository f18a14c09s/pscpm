package org.francisjohnson.pscpm.secrets.presentation.views;

public enum PSCPMViewId {
    WILDCARD("* (any view)"),
    LOGIN_FORM("Login Form"),
    SERVER_BROWSER("Server Secret Browser"),
    NEW_SERVER_FORM("New Server Secret Form"),
    SERVER_MGMT_FORM("Server Secret Mgmt Form"),
    NEW_ACCOUNT_FORM("New User Account Form"),
    ACCOUNT_MGMT_FORM("User Account Mgmt Form");
    private String displayName;

    private PSCPMViewId(String displayName) {
        setDisplayName(displayName);
    }

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
