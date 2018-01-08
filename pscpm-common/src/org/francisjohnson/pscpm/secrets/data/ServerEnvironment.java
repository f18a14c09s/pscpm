package org.francisjohnson.pscpm.secrets.data;

public enum ServerEnvironment {
    DEVELOPMENT,
    TEST,
    STAGING,
    PRODUCTION;

    private String getLabel() {
        return Character.toUpperCase(name().charAt(0)) +
            name().substring(1).toLowerCase();
    }

    public String toString() {
        return getLabel();
    }
}
