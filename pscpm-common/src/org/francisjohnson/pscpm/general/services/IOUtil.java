package org.francisjohnson.pscpm.general.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class IOUtil {

    public static boolean closeSafely(Closeable clos) {
        if (clos != null) {
            try {
                clos.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static <CloseableClass extends Closeable> List<Closeable> closeSafely(CloseableClass... closs) {
        List<Closeable> retval = new ArrayList<>();
        if (closs != null) {
            for (Closeable clos : closs) {
                if (clos != null) {
                    try {
                        clos.close();
                    } catch (Exception e) {
                        retval.add(clos);
                    }
                }
            }
        }
        return retval;
    }

    public static List<Closeable> closeSafely(List<? extends Closeable> closs) {
        List<Closeable> retval = new ArrayList<>();
        if (closs != null) {
            for (Closeable clos : closs) {
                if (clos != null) {
                    try {
                        clos.close();
                    } catch (Exception e) {
                        retval.add(clos);
                    }
                }
            }
        }
        return retval;
    }

    public static byte[] serialize(Serializable o, Logger... log) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
            return baos.toByteArray();
        }
    }

    public static <S extends Serializable> S deserialize(byte[] bytes, Logger... log) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                ObjectInputStream oos = new ObjectInputStream(bais)) {
            return (S) oos.readObject();
        }
    }

    public static <E extends Serializable, L extends List<E> & Serializable> L useArrayListIfNeeded(List<E> in) {
        if (in == null || in instanceof Serializable) {
            return (L) in;
        } else {
            L retval = (L) new ArrayList<E>();
            retval.addAll(in);
            return retval;
        }
    }
}
