package org.francisjohnson.pscpm.security.data;

public class SecurityException extends Exception {
    public SecurityException(String string, Throwable throwable, boolean b,
                             boolean b1) {
        super(string, throwable, b, b1);
    }

    public SecurityException(Throwable throwable) {
        super(throwable);
    }

    public SecurityException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public SecurityException(String string) {
        super(string);
    }

    public SecurityException() {
        super();
    }
}
