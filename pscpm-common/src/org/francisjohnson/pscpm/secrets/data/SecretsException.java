package org.francisjohnson.pscpm.secrets.data;

public class SecretsException extends Exception {
    public SecretsException(String string, Throwable throwable, boolean b,
                            boolean b1) {
        super(string, throwable, b, b1);
    }

    public SecretsException(Throwable throwable) {
        super(throwable);
    }

    public SecretsException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public SecretsException(String string) {
        super(string);
    }

    public SecretsException() {
        super();
    }
}
