package test.org.francisjohnson.pscpm;

import static org.junit.Assert.fail;

public class PSCPMTestCase {
    public static void throwRuntimeException(Exception e) {
        throw new RuntimeException(e);
    }

    public static void failWithException(Exception e) {
        e.printStackTrace();
        fail("See stack trace in stderr.  Exception message: " +
             e.getMessage());
    }
}
